package com.xingchuan.charging.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.ChargingGuns;
import com.xingchuan.charging.domain.entity.ChargingOrder;
import com.xingchuan.charging.domain.entity.ChargingPile;
import com.xingchuan.charging.domain.entity.ChargingStations;
import com.xingchuan.charging.domain.excel.ChargingPileExcelImport;
import com.xingchuan.charging.domain.model.ChargingRealtimeDataModel;
import com.xingchuan.charging.domain.req.ChargingPileAddRequest;
import com.xingchuan.charging.domain.req.ChargingPilePageListRequest;
import com.xingchuan.charging.domain.req.ChargingPileUpdateRequest;
import com.xingchuan.charging.domain.resp.ChargingGunsResponse;
import com.xingchuan.charging.domain.resp.ChargingPileDetailResponse;
import com.xingchuan.charging.domain.resp.ChargingPilePageListResponse;
import com.xingchuan.charging.enums.*;
import com.xingchuan.charging.mapper.ChargingGunsMapper;
import com.xingchuan.charging.mapper.ChargingOrderMapper;
import com.xingchuan.charging.mapper.ChargingPileMapper;
import com.xingchuan.charging.mapper.ChargingStationsMapper;
import com.xingchuan.charging.service.IChargingPileService;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.page.PageDomain;
import com.xingchuan.common.core.page.TableSupport;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 充电桩Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class ChargingPileServiceImpl extends ServiceImpl<ChargingPileMapper, ChargingPile> implements IChargingPileService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ChargingGunsMapper chargingGunsMapper;
    @Resource
    private ChargingOrderMapper chargingOrderMapper;
    @Resource
    private ChargingStationsMapper chargingStationsMapper;

    @Value("${gun-starting-number.teld}")
    private Integer teldStartingNumber;

    @Value("${gun-max-number.teld}")
    private Integer teldMaxNumber;


    /**
     * 查询充电桩列表
     */
    @Override
    public Page<ChargingPilePageListResponse> selectPageList(ChargingPilePageListRequest request) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<ChargingPilePageListResponse> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());
        return baseMapper.selectPageList(page, request);
    }

    /**
     * 获取充电桩详情
     */
    @Override
    public ChargingPileDetailResponse getChargingPileDetail(Long id) {
        ChargingPileDetailResponse response = new ChargingPileDetailResponse();
        ChargingPile chargingPile = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(chargingPile)) {
            return response;
        }
        BeanUtils.copyProperties(chargingPile, response);
        response.setPileTypeDesc(ChargePileEnum.getDescByCode(chargingPile.getPileType()));
        response.setPileStatusDesc(PileStatusEnum.getDescByCode(chargingPile.getPileStatus()));

        String protocolVersion = ProtocolTypeEnum.getDescByCode(chargingPile.getProtocolVersion());
        response.setProtocolVersion(chargingPile.getProtocolVersion());
        response.setProtocolVersionName(protocolVersion);

        ChargingStations stations = chargingStationsMapper.selectById(chargingPile.getStationId());
        response.setStationName(stations.getName());

        //返回充电设备接口类型
        response.setConnectorTypeDesc(ConnectorTypeEnum.getConnectorTypeDesc(chargingPile.getConnectorType()));
        //国家标准
        response.setNationalStandardDesc(NationalStandardEnum.getNationalStandard(chargingPile.getNationalStandard()));
        List<ChargingGuns> chargingGunsList = chargingGunsMapper.selectList(Wrappers.<ChargingGuns>lambdaQuery()
                .eq(ChargingGuns::getDeviceNo, chargingPile.getDeviceNo())
                .orderByAsc(ChargingGuns::getNo));
        if (ObjectUtils.isNotEmpty(chargingGunsList)) {
            List<ChargingGunsResponse> gunsResponseList = new ArrayList<>();
            for (ChargingGuns chargingGuns : chargingGunsList) {
                ChargingGunsResponse gunsResponse = new ChargingGunsResponse();
                BeanUtils.copyProperties(chargingGuns, gunsResponse);

                String accumulatedChargingCapacity = "--";
                String gunStatus = "--";
                String current = "--";
                String voltage = "--";
                String soc = "--";

                String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, chargingGuns.getDeviceNo(), chargingGuns.getNo());
                String jsonString = redisCache.getCacheObject(redisKey);
                if (ObjectUtils.isNotEmpty(jsonString)) {
                    ChargingRealtimeDataModel orderProcessLog = JSON.parseObject(jsonString, ChargingRealtimeDataModel.class);
                    ChargeGunsEnum processLogStatus = orderProcessLog.getStatus();

                    gunStatus = processLogStatus.getDesc();
                    accumulatedChargingCapacity = orderProcessLog.getTotalPower() + "KW";
                    current = orderProcessLog.getOutputCurrent() + "A";
                    voltage = orderProcessLog.getOutputVoltage() + "V";
                    soc = orderProcessLog.getSoc() + "%";
                }
                gunsResponse.setAccumulatedChargingCapacity(accumulatedChargingCapacity);
                gunsResponse.setGunStatus(gunStatus);
                gunsResponse.setCurrent(current);
                gunsResponse.setVoltage(voltage);
                gunsResponse.setSoc(soc);
                gunsResponseList.add(gunsResponse);
            }
            response.setGunsList(gunsResponseList);
        }
        return response;
    }

    /**
     * 新增充电桩
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addChargingPile(ChargingPileAddRequest request) {
        //判断数据库是否存在桩编码相同但未删除的充电桩
        boolean exists = baseMapper.exists(Wrappers.<ChargingPile>lambdaQuery()
                .eq(ChargingPile::getDeviceNo, request.getDeviceNo())
        );
        if (exists) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NUMBER_REPEAT);
        }
        //判断场站id是否正确
        ChargingStations stations = chargingStationsMapper.selectById(request.getStationId());
        if (ObjectUtils.isEmpty(stations)) {
            throw new ServiceException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        ChargingPile chargingPile = new ChargingPile();
        BeanUtils.copyProperties(request, chargingPile);
        chargingPile.setPileStatus(PileStatusEnum.ENABLE.getCode());
        chargingPile.setDeptId(stations.getDeptId());
        chargingPile.setTenantId(stations.getTenantId());
        baseMapper.insert(chargingPile);
        //新增充电枪
        addChargingGun(request, stations.getDeptId());
    }

    /**
     * 修改充电桩
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateChargingPile(ChargingPileUpdateRequest request) {
        //判断数据库是否存在桩编码相同但未删除的充电桩
        boolean exists = baseMapper.exists(Wrappers.<ChargingPile>lambdaQuery()
                .eq(ChargingPile::getDeviceNo, request.getDeviceNo())
                .ne(ChargingPile::getId, request.getId()));
        if (exists) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NUMBER_REPEAT);
        }
        //判断充电桩id是否正确
        ChargingPile chargingPile = baseMapper.selectById(request.getId());
        if (ObjectUtils.isEmpty(chargingPile)) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        //判断场站id是否正确
        ChargingStations stations = chargingStationsMapper.selectById(request.getStationId());
        if (ObjectUtils.isEmpty(stations)) {
            throw new ServiceException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        BeanUtils.copyProperties(request, chargingPile);

        Long gunNumber = chargingGunsMapper.selectCount(Wrappers.<ChargingGuns>lambdaQuery().eq(ChargingGuns::getDeviceNo, request.getDeviceNo()));
        chargingPile.setGunNumber(gunNumber.intValue());
        chargingPile.setTenantId(stations.getTenantId());

        baseMapper.updateById(chargingPile);
    }

    /**
     * 删除充电桩
     */
    @Override
    public void delChargingPile(Long id) {
        ChargingPile chargingPile = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(chargingPile)) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        // 判断是否存在充电枪
        boolean exists = chargingGunsMapper.exists(Wrappers.<ChargingGuns>lambdaQuery()
                .eq(ChargingGuns::getDeviceNo, chargingPile.getDeviceNo())
        );
        if (exists) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_EXIST_GUNS);
        }
        //逻辑删除充电桩
        baseMapper.delete(Wrappers.<ChargingPile>lambdaUpdate().eq(ChargingPile::getId, id));

        //逻辑删除充电桩的所有枪
        chargingGunsMapper.update(Wrappers.<ChargingGuns>lambdaUpdate().eq(ChargingGuns::getDeviceNo, chargingPile.getDeviceNo()));
    }

    /**
     * 新增充电枪-新增充电桩时使用
     */
    private void addChargingGun(ChargingPileAddRequest chargingPile, Long deptId) {
        int gunNum = teldStartingNumber;
        for (int i = 1; i < chargingPile.getGunNumber() + 1; i++) {
            ChargingGuns chargingGuns = new ChargingGuns();
            chargingGuns.setDeviceNo(chargingPile.getDeviceNo());
            chargingGuns.setDeptId(deptId);
            //判断协议类型
            if (ProtocolTypeEnum.TELD_AC.getCode() == chargingPile.getProtocolVersion()
                    || ProtocolTypeEnum.TELD_DC.getCode() == chargingPile.getProtocolVersion()) {

                if (gunNum > teldMaxNumber) {
                    throw new ServiceException(MessageConstants.CHARGING_GUN_NUMBER_MAX + gunNum);
                }
                chargingGuns.setNo(Integer.toString(gunNum));
            } else {
                if (i < 10) {
                    chargingGuns.setNo("0" + i);
                } else {
                    chargingGuns.setNo(i + "");
                }
            }
            chargingGunsMapper.insert(chargingGuns);
            gunNum++;
        }
    }

    /**
     * 新增充电枪-单独接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addChargingGun(Long id) {
        ChargingPile chargingPile = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(chargingPile)) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        ChargingGuns chargingGuns = new ChargingGuns();

        List<ChargingGuns> gunsList = chargingGunsMapper.selectList(
                Wrappers.<ChargingGuns>lambdaQuery()
                        .eq(ChargingGuns::getDeviceNo, chargingPile.getDeviceNo()));
        List<Integer> gunNoList = gunsList.stream().map(x -> Integer.parseInt(x.getNo())).distinct().sorted().collect(Collectors.toList());

        chargingGuns.setDeviceNo(chargingPile.getDeviceNo());

        String gunNo = "";
        boolean needAdd = true;
        int initValue = 0;
        int gunNumber = teldStartingNumber;
        //判断协议类型
        if (ProtocolTypeEnum.TELD_AC.getCode() == chargingPile.getProtocolVersion()
                || ProtocolTypeEnum.TELD_DC.getCode() == chargingPile.getProtocolVersion()) {
            while (needAdd) {
                if (!gunNoList.contains(gunNumber)) {
                    needAdd = false;
                    gunNo = Integer.toString(gunNumber);
                    if (gunNumber > teldMaxNumber) {
                        throw new ServiceException(MessageConstants.CHARGING_GUN_NUMBER_MAX + gunNumber);
                    }
                } else {
                    gunNumber++;
                }
            }
        } else {
            while (needAdd) {
                initValue++;
                if (!gunNoList.contains(initValue)) {
                    needAdd = false;
                    gunNo = Integer.toString(initValue);
                    if (initValue < 10) {
                        gunNo = "0" + initValue;
                    }
                }
            }
        }
        chargingGuns.setNo(gunNo);
        chargingGuns.setDeptId(chargingPile.getDeptId());
        chargingGunsMapper.insert(chargingGuns);

        //更新充电桩的枪数量
        chargingPile.setGunNumber(gunNoList.size() + 1);
        baseMapper.updateById(chargingPile);
    }

    /**
     * 删除充电枪
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delChargingGun(Long id) {
        ChargingGuns chargingGuns = chargingGunsMapper.selectOne(Wrappers.<ChargingGuns>lambdaQuery()
                .select(ChargingGuns::getNo, ChargingGuns::getDeviceNo)
                .eq(ChargingGuns::getId, id)
        );
        if (ObjectUtils.isEmpty(chargingGuns)) {
            throw new ServiceException(MessageConstants.CHARGING_GUN_NOT_EXIST);
        }

        ChargingPile chargingPile = baseMapper.selectOne(Wrappers.<ChargingPile>lambdaQuery()
                .eq(ChargingPile::getDeviceNo, chargingGuns.getDeviceNo()));
        if (ObjectUtils.isEmpty(chargingPile)) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        //判断充电枪是否有订单
        boolean exists = chargingOrderMapper.exists(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getStationId, chargingPile.getStationId())
                .eq(ChargingOrder::getDeviceNo, chargingGuns.getDeviceNo())
                .eq(ChargingOrder::getGunNo, chargingGuns.getNo()));
        if (exists) {
            throw new ServiceException(MessageConstants.CHARGING_GUN_EXIST_ORDER);
        }
        // 删除充电枪
        chargingGunsMapper.deleteById(id);

        //更新充电桩的枪数量
        Long count = chargingGunsMapper.selectCount(Wrappers.<ChargingGuns>lambdaQuery()
                .eq(ChargingGuns::getDeviceNo, chargingPile.getDeviceNo())
        );
        chargingPile.setGunNumber(Math.toIntExact(count));
        baseMapper.updateById(chargingPile);
    }

    /**
     * 导入充电桩数据
     *
     * @param pileList 桩数据
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importChargingPile(List<ChargingPileExcelImport> pileList) {
        if (CollectionUtils.isEmpty(pileList)) {
            throw new ServiceException("导入充电桩数据不能为空！");
        }
        // 查询场站
        Map<String, ChargingStations> stationMap = new HashMap<>();

        Set<String> stationNameSet = pileList.stream().map(ChargingPileExcelImport::getStationName).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(stationNameSet)) {
            stationMap = chargingStationsMapper.selectList(Wrappers.<ChargingStations>lambdaQuery()
                    .select(ChargingStations::getId, ChargingStations::getName, ChargingStations::getDeptId, ChargingStations::getTenantId)
                    .in(ChargingStations::getName, stationNameSet)).stream().collect(Collectors.toMap(ChargingStations::getName, pro -> pro));
        }
        StringBuilder errorMsg = new StringBuilder();

        int line = 1;
        for (ChargingPileExcelImport pile : pileList) {
            boolean isAdded = true;
            // 校验场站是否存在
            String stationName = pile.getStationName();
            if (StringUtils.isBlank(stationName)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：场站名称不能为空<br>");
            }
            ChargingStations stations = stationMap.get(stationName);
            if (ObjectUtils.isEmpty(stations)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：场站不存在<br>");
            }
            // 校验充电桩编号
            String deviceNo = pile.getDeviceNo();
            if (StringUtils.isBlank(deviceNo)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：充电桩编号不能为空<br>");
            }
            // 充电桩类型
            String pileType = pile.getPileType();
            if (StringUtils.isBlank(pileType)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：充电桩类型不能为空<br>");
            }
            Integer pileTypeCode = ChargePileEnum.getCodeByDesc(pileType);
            if (ObjectUtils.isEmpty(pileTypeCode)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：充电桩类型不能为空<br>");
            }
            // 充电桩名称
            String name = pile.getName();
            if (StringUtils.isBlank(name)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：充电桩名称不能为空<br>");
            }
            // 枪数量
            int gunNumber = ObjectUtils.isEmpty(pile.getGunNumber()) ? 0 : pile.getGunNumber();
            // 硬件版本
            String hardwareVersion = pile.getHardwareVersion();
            if (StringUtils.isBlank(hardwareVersion)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：硬件版本不能为空<br>");
            }
            // 输出电流
            BigDecimal maxCurrent = ObjectUtils.isEmpty(pile.getMaxCurrent()) ? BigDecimal.ZERO : pile.getMaxCurrent();
            BigDecimal minCurrent = ObjectUtils.isEmpty(pile.getMinCurrent()) ? BigDecimal.ZERO : pile.getMinCurrent();
            // 输出电压
            BigDecimal maxVoltage = ObjectUtils.isEmpty(pile.getMaxVoltage()) ? BigDecimal.ZERO : pile.getMaxVoltage();
            BigDecimal minVoltage = ObjectUtils.isEmpty(pile.getMinVoltage()) ? BigDecimal.ZERO : pile.getMinVoltage();
            // 输出功率
            int maxPower = ObjectUtils.isEmpty(pile.getMaxPower()) ? 0 : pile.getMaxPower();
            int minPower = ObjectUtils.isEmpty(pile.getMinPower()) ? 0 : pile.getMinPower();
            // 协议版本
            String protocolVersion = pile.getProtocolVersion();
            if (StringUtils.isBlank(protocolVersion)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：协议版本不能为空<br>");
            }
            // 生产厂商
            String supplier = pile.getSupplier();
            if (StringUtils.isBlank(supplier)) {
                isAdded = false;
                errorMsg.append("第").append(line).append("行：生产厂商不能为空<br>");
            }
            // 备注
            String remark = pile.getRemark();
            if (isAdded) {
                ChargingPile chargingPile = baseMapper.selectOne(Wrappers.<ChargingPile>lambdaQuery().eq(ChargingPile::getDeviceNo, deviceNo));
                if (ObjectUtils.isEmpty(chargingPile)) {
                    chargingPile = new ChargingPile();
                }
                chargingPile.setName(name);
                chargingPile.setRemark(remark);
                chargingPile.setDeviceNo(deviceNo);
                chargingPile.setMaxPower(maxPower);
                chargingPile.setMinPower(minPower);
                chargingPile.setSupplier(supplier);
                chargingPile.setGunNumber(gunNumber);
                chargingPile.setMaxCurrent(maxCurrent);
                chargingPile.setMinCurrent(minCurrent);
                chargingPile.setMaxVoltage(maxVoltage);
                chargingPile.setMinVoltage(minVoltage);
                chargingPile.setPileType(pileTypeCode);
                chargingPile.setStationId(stations.getId());
                chargingPile.setDeptId(stations.getDeptId());
                chargingPile.setTenantId(stations.getTenantId());
                chargingPile.setHardwareVersion(hardwareVersion);
                chargingPile.setPileStatus(PileStatusEnum.ENABLE.getCode());
                chargingPile.setProtocolVersion(ProtocolTypeEnum.getCodeByEnumName(protocolVersion));

                baseMapper.insert(chargingPile);
            }
            line++;
        }
        return errorMsg.toString();
    }

    /**
     * 充电桩-修改启用、停用
     *
     * @param id 桩id
     */
    @Override
    public void updateShowStatus(Long id) {

        ChargingPile chargingPile = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(chargingPile)) {
            throw new RuntimeException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        int pileStatus = PileStatusEnum.DISABLE.getCode();
        if (chargingPile.getPileStatus().equals(PileStatusEnum.DISABLE.getCode())) {
            pileStatus = PileStatusEnum.ENABLE.getCode();
        }
        chargingPile.setPileStatus(pileStatus);
        baseMapper.updateById(chargingPile);
    }

}
