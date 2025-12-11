package com.xingchuan.charging.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.*;
import com.xingchuan.charging.domain.model.ChargingPileModel;
import com.xingchuan.charging.domain.model.ChargingRealtimeData;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.enums.*;
import com.xingchuan.charging.mapper.*;
import com.xingchuan.charging.service.IAreaService;
import com.xingchuan.charging.service.IChargingStationsService;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.model.AreaModel;
import com.xingchuan.common.core.domain.model.QRCodeParam;
import com.xingchuan.common.core.page.PageDomain;
import com.xingchuan.common.core.page.TableSupport;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.PageUtils;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.common.utils.WxQRCodeUtil;
import com.xingchuan.common.utils.bean.BeanUtils;
import com.xingchuan.system.service.IQrCodeRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 场站Service业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class ChargingStationsServiceImpl extends ServiceImpl<ChargingStationsMapper, ChargingStations>
        implements IChargingStationsService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private RulesMapper rulesMapper;
    @Resource
    private IAreaService areaService;
    @Resource
    private RuleTimeMapper ruleTimeMapper;
    @Resource
    private ChargingGunsMapper gunsMapper;
    @Resource
    private ChargingPileMapper pileMapper;
    @Resource
    private IQrCodeRuleService qrCodeRuleService;
    @Resource
    private ChargingOrderMapper chargingOrderMapper;
    @Resource
    private ChargingStationAnnexMapper stationAnnexMapper;

    /**
     * 设置时间格式为 HH:mm:ss
     */
    private static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("HH:mm");
    }

    /**
     * 查询场站列表
     *
     * @param request 场站
     * @return 场站
     */
    @Override
    public Page<ChargingStationsPageResponse> selectChargingStationsList(ChargingStationsPageRequest request) {
        Page<ChargingStationsPageResponse> responsePage = new Page<>();

        Page<ChargingStations> chargingStationsPage = baseMapper.selectPage(PageUtils.getPageInfo(),
                Wrappers.<ChargingStations>lambdaQuery()
                        .select(ChargingStations::getId,
                                ChargingStations::getProvince,
                                ChargingStations::getCity,
                                ChargingStations::getAddress,
                                ChargingStations::getLat,
                                ChargingStations::getLng,
                                ChargingStations::getName,
                                ChargingStations::getPlugCharge,
                                ChargingStations::getStarLabel,
                                ChargingStations::getServiceLabel,
                                ChargingStations::getFacilityLabel,
                                ChargingStations::getShowStatus,
                                ChargingStations::getTenantId,
                                ChargingStations::getOperatorStationId,
                                ChargingStations::getRuleId)
                        .like(ObjectUtils.isNotEmpty(request.getName()), ChargingStations::getName, request.getName())
                        .eq(ObjectUtils.isNotEmpty(request.getTenantId()), ChargingStations::getTenantId,
                                request.getTenantId())
                        .orderByDesc(ChargingStations::getCreateTime));
        if (chargingStationsPage.getTotal() <= 0) {
            return responsePage;
        }
        List<ChargingStations> chargingStationsList = chargingStationsPage.getRecords();

        // 获取省市区信息
        List<AreaModel> areaModels = areaService.getAreaList();

        // 查询计费规则id
        Set<Long> ruleIds = chargingStationsList.stream().map(ChargingStations::getRuleId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // 查询计费规则名称
        Map<Long, String> ruleMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ruleIds)) {
            ruleMap = rulesMapper.selectList(Wrappers.<Rules>lambdaQuery()
                    .select(Rules::getId, Rules::getName).in(Rules::getId, ruleIds)).stream()
                    .collect(Collectors.toMap(Rules::getId, Rules::getName));
        }
        responsePage.setTotal(chargingStationsPage.getTotal());
        Map<Long, String> finalRuleMap = ruleMap;
        responsePage.setRecords(chargingStationsList.stream().map(item -> {
            ChargingStationsPageResponse response = new ChargingStationsPageResponse();
            BeanUtils.copyBeanProp(response, item);

            areaModels.stream().filter(area -> area.getCode().equals(item.getCity())).findFirst()
                    .ifPresent(area -> response.setLocation(area.getFullName()));

            response.setCoord(response.getLng() + "," + response.getLat());
            // 是否互联互通
            response.setOperateStation(ObjectUtils.isNotEmpty(item.getOperatorStationId()));

            String ruleName = finalRuleMap.get(item.getRuleId());
            response.setRuleName(ruleName);

            return response;
        }).collect(Collectors.toList()));
        return responsePage;
    }

    /**
     * 根据id查询场站详情对象
     *
     * @param id 场站id
     * @return 结果
     */
    @Override
    public ChargingStationsDetailResponse selectChargingStationsById(Long id) {
        ChargingStationsDetailResponse response = new ChargingStationsDetailResponse();

        ChargingStations chargingStations = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(chargingStations)) {
            return response;
        }
        Rules rules = rulesMapper.selectById(chargingStations.getRuleId());
        BeanUtils.copyBeanProp(response, chargingStations);
        response.setRuleName(ObjectUtils.isNotEmpty(rules) ? rules.getName() : null);

        // 附件
        Map<Integer, List<String>> annexMap = stationAnnexMapper.selectList(Wrappers.<ChargingStationAnnex>lambdaQuery()
                .select(ChargingStationAnnex::getId,
                        ChargingStationAnnex::getType,
                        ChargingStationAnnex::getImage)
                .eq(ChargingStationAnnex::getStationId, id)).stream()
                .collect(Collectors.groupingBy(ChargingStationAnnex::getType,
                        Collectors.mapping(ChargingStationAnnex::getImage, Collectors.toList())));

        if (ObjectUtils.isNotEmpty(annexMap)) {
            // logo
            List<String> logos = annexMap.get(StationAnnexEnum.LOGO.getCode());
            if (CollectionUtils.isNotEmpty(logos)) {
                response.setLogo(logos.get(0));
            }
            // 场站图片
            response.setStationImageList(annexMap.get(StationAnnexEnum.STATION_IMAGE.getCode()));
            // 建设场所
            response.setConstruction(chargingStations.getConstruction());
            // 营业执照
            List<String> licenseImages = annexMap.get(StationAnnexEnum.BUSINESS_LICENSE.getCode());
            if (CollectionUtils.isNotEmpty(licenseImages)) {
                response.setLicenseImage(licenseImages.get(0));
            }
        }

        return response;
    }

    /**
     * 新增场站
     *
     * @param request 新增场站
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertChargingStations(ChargingStationsAddRequest request) {
        // 校验
        checkChargingStations(request);
        ChargingStations chargingStations = new ChargingStations();

        if (StringUtils.isNotBlank(request.getServiceLabel())) {
            request.setServiceLabel(request.getServiceLabel().replaceAll(" ", ""));
        }
        if (StringUtils.isNotBlank(request.getFacilityLabel())) {
            request.setFacilityLabel(request.getFacilityLabel().replaceAll(" ", ""));
        }
        if (StringUtils.isNotBlank(request.getParkingFeeLabel())) {
            request.setParkingFeeLabel(request.getParkingFeeLabel().replaceAll(" ", ""));
        }

        Long deptId = Objects.requireNonNull(SecurityUtils.getLoginUser()).getDeptId();

        BeanUtils.copyBeanProp(chargingStations, request);

        chargingStations.setDeptId(deptId);
        int insert = baseMapper.insert(chargingStations);

        // 附件
        request.setId(chargingStations.getId());
        saveStationAnnex(request, deptId);
        return insert;
    }

    /**
     * 修改场站信息
     *
     * @param request 修改场站
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateChargingStations(ChargingStationsAddRequest request) {
        Long stationId = request.getId();

        ChargingStations chargingStations = baseMapper.selectById(stationId);
        if (ObjectUtils.isEmpty(chargingStations)) {
            throw new RuntimeException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        BeanUtils.copyBeanProp(chargingStations, request);
        // 附件
        saveStationAnnex(request, chargingStations.getDeptId());

        return baseMapper.updateById(chargingStations);
    }

    /**
     * 删除
     *
     * @param id 场站id
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteChargingStations(Long id) {
        // 需要判断是是否充过电，充过电就不允许删除
        boolean existsOrder = chargingOrderMapper.exists(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getStationId, id));
        if (existsOrder) {
            throw new ServiceException(MessageConstants.STATION_EXIST_ORDER);
        }
        // 判断是否有充电桩
        boolean existsPile = pileMapper.exists(Wrappers.<ChargingPile>lambdaQuery()
                .eq(ChargingPile::getStationId, id));
        if (existsPile) {
            throw new ServiceException(MessageConstants.STATION_EXIST_PILE);
        }
        // 附件
        baseMapper.delete(Wrappers.<ChargingStations>lambdaUpdate().eq(ChargingStations::getId, id));
        stationAnnexMapper
                .delete(Wrappers.<ChargingStationAnnex>lambdaUpdate().eq(ChargingStationAnnex::getStationId, id));
    }

    /**
     * 场站管理-是否展示状态切换
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShowStatus(Long id) {
        ChargingStations stations = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(stations)) {
            throw new ServiceException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }

        stations.setShowStatus(StationShowStatusEnum.getByCode(stations.getShowStatus()));
        baseMapper.updateById(stations);
    }

    /**
     * 查询场站下拉列表
     */
    @Override
    public List<StationsResponse> getStationList() {
        List<StationsResponse> responseList = new ArrayList<>();

        List<ChargingStations> stationsList = baseMapper.selectList(Wrappers.<ChargingStations>lambdaQuery()
                .select(ChargingStations::getId, ChargingStations::getName, ChargingStations::getOperatorStationId)
                .orderByDesc(ChargingStations::getCreateTime));

        if (ObjectUtils.isNotEmpty(stationsList)) {
            for (ChargingStations stations : stationsList) {
                StationsResponse response = new StationsResponse();
                BeanUtils.copyProperties(stations, response);
                // 是否互联互通
                response.setOperateStation(ObjectUtils.isNotEmpty(stations.getOperatorStationId()));
                responseList.add(response);
            }
        }
        return responseList;
    }

    /**
     * 根据运营商查询场站下拉列表
     */
    @Override
    public List<StationsResponse> getListByTenantId() {
        List<StationsResponse> responseList = new ArrayList<>();

        List<ChargingStations> stationsList = baseMapper.selectList(Wrappers.<ChargingStations>lambdaQuery()
                .select(ChargingStations::getId, ChargingStations::getName, ChargingStations::getOperatorStationId)
                .orderByDesc(ChargingStations::getCreateTime));

        if (ObjectUtils.isNotEmpty(stationsList)) {
            for (ChargingStations stations : stationsList) {
                StationsResponse response = new StationsResponse();
                BeanUtils.copyProperties(stations, response);
                // 是否互联互通
                response.setOperateStation(ObjectUtils.isNotEmpty(stations.getOperatorStationId()));
                responseList.add(response);
            }
        }
        return responseList;
    }

    /**
     * 小程序-查询场站列表
     */
    @Override
    public Page<StationsPageResponse> selectMiniStationList(StationsPageRequest request) {
        Page<StationsPageResponse> responsePage = new Page<>();
        // 分页
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageSize = pageDomain.getPageSize();
        Integer pageNum = (pageDomain.getPageNum() - 1) * pageSize;

        Long total = baseMapper.listChargingStationsCount(request, pageNum, pageSize);
        if (total == null || total <= 0) {
            return new Page<>();
        }
        List<StationsPageResponse> responseList = baseMapper.listChargingStations(request, pageNum, pageSize);
        if (CollectionUtils.isEmpty(responseList)) {
            return responsePage;
        }
        // 获取各个站的充电枪信息
        List<Long> chargingStationIds = responseList.stream().map(StationsPageResponse::getId)
                .collect(Collectors.toList());
        Map<Long, List<ChargingPileModel>> chargingPileMap = baseMapper
                .listStationDeviceByStationIds(chargingStationIds).stream()
                .collect(Collectors.groupingBy(ChargingPileModel::getStationId));
        for (StationsPageResponse record : responseList) {
            List<ChargingPileModel> chargingPileModelList = chargingPileMap.get(record.getId());
            int fastCharging = 0;
            int slowCharging = 0;
            int idleFastChargeCount = 0;
            int idleSlowChargeCount = 0;
            if (CollectionUtils.isNotEmpty(chargingPileModelList)) {
                for (ChargingPileModel chargingPileModel : chargingPileModelList) {
                    // 获取redis中枪的状态
                    String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, chargingPileModel.getDeviceNo(),
                            chargingPileModel.getGunsNo());
                    String cacheObject = redisCache.getCacheObject(redisKey);
                    if (ObjectUtils.isNotEmpty(cacheObject)) {
                        ChargingRealtimeData orderProcessLog = JSON.parseObject(cacheObject,
                                ChargingRealtimeData.class);
                        ChargeGunsEnum processLogStatus = orderProcessLog.getStatus();
                        if (processLogStatus.equals(ChargeGunsEnum.ONLINE)) {
                            if (Objects.equals(chargingPileModel.getPileType(), ChargePileEnum.DC.getCode())) {
                                idleFastChargeCount++;
                            } else {
                                idleSlowChargeCount++;
                            }
                        }
                    }
                    // 快充
                    if (Objects.equals(chargingPileModel.getPileType(), ChargePileEnum.DC.getCode())) {
                        fastCharging++;
                    } else {
                        slowCharging++;
                    }
                }
            }
            record.setFastCharging(fastCharging);
            record.setSlowCharging(slowCharging);
            record.setIdleFastChargeCount(idleFastChargeCount);
            record.setIdleSlowChargeCount(idleSlowChargeCount);
            // 充电类型标签
            String chargingType = (record.getMaxPower() < 30) ? "慢充" : "快充,慢充";
            record.setChargingType(chargingType);
        }
        responsePage.setTotal(total);
        responsePage.setRecords(responseList);
        return responsePage;
    }

    /**
     * 查询地图场站列表
     *
     * @param request 请求参数
     * @return 结果
     */
    @Override
    public List<StationsPageResponse> listMapStation(MapChargingStationsPageRequest request) {
        List<StationsPageResponse> responseList = baseMapper.listMapStation(request);
        if (CollectionUtils.isEmpty(responseList)) {
            return Collections.emptyList();
        }
        // 获取各个站的充电枪信息
        List<Long> chargingStationIds = responseList.stream().map(StationsPageResponse::getId)
                .collect(Collectors.toList());
        Map<Long, List<ChargingPileModel>> chargingPileMap = baseMapper
                .listStationDeviceByStationIds(chargingStationIds).stream()
                .collect(Collectors.groupingBy(ChargingPileModel::getStationId));
        for (StationsPageResponse record : responseList) {
            List<ChargingPileModel> chargingPileModelList = chargingPileMap.get(record.getId());

            int fastCharging = 0;
            int slowCharging = 0;
            int idleFastChargeCount = 0;
            int idleSlowChargeCount = 0;
            if (CollectionUtils.isNotEmpty(chargingPileModelList)) {
                for (ChargingPileModel chargingPileModel : chargingPileModelList) {
                    // 获取redis中枪的状态
                    String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, chargingPileModel.getDeviceNo(),
                            chargingPileModel.getGunsNo());
                    String cacheObject = redisCache.getCacheObject(redisKey);
                    if (ObjectUtils.isNotEmpty(cacheObject)) {
                        ChargingRealtimeData orderProcessLog = JSON.parseObject(cacheObject,
                                ChargingRealtimeData.class);
                        ChargeGunsEnum processLogStatus = orderProcessLog.getStatus();
                        if (processLogStatus.equals(ChargeGunsEnum.ONLINE)) {
                            if (Objects.equals(chargingPileModel.getPileType(), ChargePileEnum.DC.getCode())) {
                                idleFastChargeCount++;
                            } else {
                                idleSlowChargeCount++;
                            }
                        }
                    }
                    // 快充
                    if (Objects.equals(chargingPileModel.getPileType(), ChargePileEnum.DC.getCode())) {
                        fastCharging++;

                    } else {
                        slowCharging++;

                    }
                }
            }
            record.setFastCharging(fastCharging);
            record.setSlowCharging(slowCharging);
            record.setIdleFastChargeCount(idleFastChargeCount);
            record.setIdleSlowChargeCount(idleSlowChargeCount);
            // 充电类型标签
            String chargingType = (record.getMaxPower() < 30) ? "慢充" : "快充,慢充";
            record.setChargingType(chargingType);
        }
        return responseList;
    }

    /**
     * 根据id查询场站详情对象
     *
     * @param request 查询条件
     * @return 结果
     */
    @Override
    public MiniStationsDetailResponse selectMiniStationDetailById(ChargingStationsDetailRequest request) {
        // 查询
        Long id = request.getId();
        MiniStationsDetailResponse response = baseMapper.getMiniStationInfoById(id, request.getLat(), request.getLng());
        if (ObjectUtils.isEmpty(response)) {
            return response;
        }
        // 获取各个站的充电枪信息
        List<ChargingPileModel> pileModelList = baseMapper.listStationDeviceByStationId(id);
        int fastCharging = 0;
        int slowCharging = 0;
        int fastChargingIdle = 0;
        int slowChargingIdle = 0;
        if (CollectionUtils.isNotEmpty(pileModelList)) {
            for (ChargingPileModel chargingPileModel : pileModelList) {
                // 获取redis中枪的状态
                String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, chargingPileModel.getDeviceNo(),
                        chargingPileModel.getGunsNo());
                String cacheObject = redisCache.getCacheObject(redisKey);
                if (ObjectUtils.isNotEmpty(cacheObject)) {
                    ChargingRealtimeData orderProcessLog = JSON.parseObject(cacheObject, ChargingRealtimeData.class);
                    ChargeGunsEnum processLogStatus = orderProcessLog.getStatus();
                    if (processLogStatus.equals(ChargeGunsEnum.ONLINE)) {
                        if (Objects.equals(chargingPileModel.getPileType(), ChargePileEnum.DC.getCode())) {
                            fastChargingIdle++;
                        } else {
                            slowChargingIdle++;
                        }
                    }
                }
                // 快充
                if (Objects.equals(chargingPileModel.getPileType(), ChargePileEnum.DC.getCode())) {
                    fastCharging++;
                } else {
                    slowCharging++;
                }
            }
        }
        response.setFastCharging(fastCharging);
        response.setSlowCharging(slowCharging);
        response.setFastChargingIdle(fastChargingIdle);
        response.setSlowChargingIdle(slowChargingIdle);
        // 设置时间格式为 HH:mm:ss
        SimpleDateFormat sdf = getSimpleDateFormat();
        Long ruleId = response.getRuleId();
        if (ObjectUtils.isNotEmpty(ruleId)) {
            RuleDetails ruleDetail = ruleTimeMapper.getCurrentPeriodCostByRuleId(ruleId, new Date());
            if (ObjectUtils.isNotEmpty(ruleDetail)) {
                response.setChargeFee(ruleDetail.getChargeFee().add(ruleDetail.getServiceFee()));
                response.setCurrentTime(
                        sdf.format(ruleDetail.getStartTime()) + "-" + sdf.format(ruleDetail.getEndTime()));
            }
        }
        // 查询附件信息
        Map<Integer, List<String>> annexMap = stationAnnexMapper.selectList(Wrappers.<ChargingStationAnnex>lambdaQuery()
                .select(ChargingStationAnnex::getId, ChargingStationAnnex::getType, ChargingStationAnnex::getImage)
                .eq(ChargingStationAnnex::getStationId, id)).stream()
                .collect(Collectors.groupingBy(ChargingStationAnnex::getType,
                        Collectors.mapping(ChargingStationAnnex::getImage, Collectors.toList())));

        if (ObjectUtils.isNotEmpty(annexMap)) {
            // logo
            List<String> logos = annexMap.get(StationAnnexEnum.LOGO.getCode());
            if (CollectionUtils.isNotEmpty(logos)) {
                response.setLogo(logos.get(0));
            }
            // 场站图片
            response.setStationImageList(annexMap.get(StationAnnexEnum.STATION_IMAGE.getCode()));
            // 营业执照
            List<String> licenseImages = annexMap.get(StationAnnexEnum.BUSINESS_LICENSE.getCode());
            if (CollectionUtils.isNotEmpty(licenseImages)) {
                response.setLicenseImage(licenseImages.get(0));
            }
        }
        return response;
    }

    /**
     * 获取场站列表
     *
     * @return 结果
     */
    @Override
    public List<MiniRuleDetailsResponse> getStationRuleById(Long id) {
        ChargingStations chargingStations = baseMapper.selectOne(Wrappers.<ChargingStations>lambdaQuery()
                .select(ChargingStations::getRuleId).eq(ChargingStations::getId, id));
        if (ObjectUtils.isEmpty(chargingStations)) {
            throw new RuntimeException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        List<RuleDetails> detailsList = ruleTimeMapper.listCurrentPeriodCostByRuleId(chargingStations.getRuleId());
        if (CollectionUtils.isEmpty(detailsList)) {
            throw new RuntimeException(MessageConstants.RULE_NOT_EXIST);
        }
        // 获取最低金额
        RuleDetails model = detailsList.get(0);
        BigDecimal lowestPrice = model.getChargeFee().add(model.getServiceFee());
        List<MiniRuleDetailsResponse> responseList = new ArrayList<>();
        for (RuleDetails details : detailsList) {
            MiniRuleDetailsResponse response = new MiniRuleDetailsResponse();
            response.setElectricityFee(details.getChargeFee());
            response.setServiceFee(details.getServiceFee());
            response.setTotalAmount(details.getChargeFee().add(details.getServiceFee()));
            response.setType(RuleTypeEnum.getDescByCode(details.getType()));

            // 设置时间格式为 HH:mm:ss
            SimpleDateFormat sdf = getSimpleDateFormat();
            String format = sdf.format(new Date());
            try {
                Date currTime = sdf.parse(format);
                if (currTime.equals(details.getStartTime())
                        || (details.getStartTime().before(currTime) && details.getEndTime().after(currTime))) {
                    response.setCurrentTimePeriod(true);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            response.setTimePeriod(sdf.format(details.getStartTime()) + "-" + sdf.format(details.getEndTime()));
            // 寻找最低金额
            if (lowestPrice.compareTo(details.getChargeFee().add(details.getServiceFee())) > -1) {
                lowestPrice = details.getChargeFee().add(details.getServiceFee());
            }
            responseList.add(response);
        }
        // 最低价格
        for (MiniRuleDetailsResponse response : responseList) {
            if (response.getTotalAmount().compareTo(lowestPrice) == 0) {
                response.setLowestPrice(true);
            }
        }
        return responseList;
    }

    /**
     * 根据场站id查询场站设备信息
     *
     * @param stationId 场站id
     * @param type      快充、慢充
     * @return 结果
     */
    @Override
    public List<ChargingStationDeviceResponse> getStationDevice(Long stationId, ChargePileEnum type) {
        List<ChargingStationDeviceResponse> responseList = new ArrayList<>();
        List<ChargingPileModel> chargingPileModelList = baseMapper.listStationDeviceByStationIdAndType(stationId, type);
        if (ObjectUtils.isEmpty(chargingPileModelList)) {
            return responseList;
        }
        // 查询桩信息
        for (ChargingPileModel chargingPileModel : chargingPileModelList) {
            String deviceNo = chargingPileModel.getDeviceNo();
            String gunsNo = chargingPileModel.getGunsNo();

            ChargingStationDeviceResponse response = new ChargingStationDeviceResponse();
            BeanUtils.copyBeanProp(response, chargingPileModel);
            ChargeGunsEnum processLogStatus = ChargeGunsEnum.OFFLINE;
            // 获取redis中枪的状态
            String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, deviceNo, gunsNo);
            String cacheObject = redisCache.getCacheObject(redisKey);
            if (ObjectUtils.isNotEmpty(cacheObject)) {
                ChargingRealtimeData orderProcessLog = JSON.parseObject(cacheObject, ChargingRealtimeData.class);
                processLogStatus = orderProcessLog.getStatus();
                response.setRatio(BigDecimal.valueOf(orderProcessLog.getSoc()));
            }
            response.setDeviceStatus(processLogStatus.getCode());
            response.setDeviceStatusName(processLogStatus.getDesc());

            response.setDeviceNo(deviceNo + gunsNo);
            response.setPileType(ChargePileEnum.getDescByCode(chargingPileModel.getPileType()));
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * 根据传入枪号查询充电确认信息
     *
     * @param code 二维码参数
     * @return 结果
     */
    @Override
    public ReadyChargingResponse readyCharge(String code) {
        // 解析二维码
        QRCodeResult result = qrCodeRuleService.parseQRCode(code);
        String deviceNo = result.getDeviceNo();
        String gunsNo = result.getGunsNo();

        if (StringUtils.isEmpty(gunsNo)) {
            throw new RuntimeException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        ReadyChargingResponse response;

        // 查询桩信息
        if (StringUtils.isNotEmpty(deviceNo)) {
            response = baseMapper.getChargePreparationInfo(deviceNo, gunsNo);
        } else {
            response = baseMapper.getChargePreparationInfoByDeviceNoAndGunsNo(gunsNo);
        }
        if (ObjectUtils.isEmpty(response)) {
            throw new RuntimeException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        if (ObjectUtils.isEmpty(deviceNo)) {
            deviceNo = response.getDeviceNo();
        }
        Long stationId = response.getStationId();
        // 查询是否存在充电中的订单
        ChargingOrder chargingOrder = chargingOrderMapper.selectOne(Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getTradeNo)
                .in(ChargingOrder::getOrderState, OrderStatusEnum.NO_START.getCode(),
                        OrderStatusEnum.CHARGING.getCode())
                .eq(ChargingOrder::getOpenId, SecurityUtils.getUserOpenId())
                .eq(ChargingOrder::getStationId, stationId)
                .eq(ChargingOrder::getDeviceNo, deviceNo).eq(ChargingOrder::getGunNo, gunsNo)
                .last("LIMIT 1"));
        if (ObjectUtils.isNotEmpty(chargingOrder)) {
            response.setTradeNo(chargingOrder.getTradeNo());
        }
        // 获取redis中枪的状态
        ChargeGunsEnum gunsEnum = ChargeGunsEnum.OFFLINE;
        String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, response.getOperatorId(), deviceNo, gunsNo);
        String cacheObject = redisCache.getCacheObject(redisKey);
        if (ObjectUtils.isNotEmpty(cacheObject)) {
            log.info("redis中枪的状态为：{}", cacheObject);
            ChargingRealtimeData orderProcessLog = JSON.parseObject(cacheObject, ChargingRealtimeData.class);
            // 判断枪状态
            gunsEnum = orderProcessLog.getStatus();
        }
        response.setGunsStatus(gunsEnum.getDesc());
        // 是否快充
        boolean fastCharging = response.getMaxPower() >= 30;
        response.setFastCharging(fastCharging);

        // 设置时间格式为 HH:mm:ss
        SimpleDateFormat sdf = getSimpleDateFormat();

        if (ObjectUtils.isNotEmpty(response.getRuleId())) {
            RuleDetails ruleDetails = ruleTimeMapper.getCurrentPeriodCostByRuleId(response.getRuleId(), new Date());
            if (ObjectUtils.isNotEmpty(ruleDetails)) {
                response.setChargeFee(ruleDetails.getChargeFee().add(ruleDetails.getServiceFee()));
                response.setCurrentTime(
                        sdf.format(ruleDetails.getStartTime()) + "-" + sdf.format(ruleDetails.getEndTime()));
            }
        }
        return response;
    }

    /**
     * 生成场站下所有枪的微信二维码
     */
    @Override
    public void generateQRCodeZip(Long stationId, HttpServletResponse servletResponse) {
        ChargingStations stations = baseMapper.selectById(stationId);
        if (ObjectUtils.isEmpty(stations)) {
            throw new ServiceException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        List<ChargingPile> chargingPileList = pileMapper.selectList(Wrappers.<ChargingPile>lambdaQuery()
                .select(ChargingPile::getDeviceNo)
                .eq(ChargingPile::getStationId, stationId));
        if (ObjectUtils.isEmpty(chargingPileList)) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        List<QRCodeParam> codeParamList = new ArrayList<>();
        String appQRCodeUrl = "https://wxapi.xxx.com?code=%sG%s";
        for (ChargingPile pile : chargingPileList) {
            List<ChargingGuns> chargingGunList = gunsMapper.selectList(Wrappers.<ChargingGuns>lambdaQuery()
                    .select(ChargingGuns::getNo).eq(ChargingGuns::getDeviceNo, pile.getDeviceNo()));
            if (CollectionUtils.isNotEmpty(chargingGunList)) {
                for (ChargingGuns chargingGuns : chargingGunList) {
                    String gunNo = chargingGuns.getNo();
                    QRCodeParam qrCodeParam = new QRCodeParam();

                    qrCodeParam.setContent(String.format(appQRCodeUrl, pile.getDeviceNo(), gunNo));

                    String name = stations.getName() + "_" + pile.getDeviceNo() + "_" + gunNo + ".png";
                    qrCodeParam.setName(name);

                    codeParamList.add(qrCodeParam);
                }
            }
        }
        try {
            // 核心代码-生成二维码
            if (CollectionUtils.isEmpty(codeParamList)) {
                throw new ServiceException(MessageConstants.CHARGING_GUN_NOT_EXIST);
            }
            WxQRCodeUtil.createZipOfQRCodes(codeParamList, servletResponse.getOutputStream());
        } catch (Exception e) {
            log.error("生成二维码失败", e);
            throw new RuntimeException("二维码生成失败，请查看日志");
        }
    }

    /**
     * 校验场站信息
     *
     * @param request 场站信息
     */
    private void checkChargingStations(ChargingStationsAddRequest request) {
        // 场站名称不能重复
        if (baseMapper
                .exists(Wrappers.<ChargingStations>lambdaQuery().eq(ChargingStations::getName, request.getName()))) {
            throw new RuntimeException(MessageConstants.CHARGING_STATIONS_EXIST);
        }
        // 省市区是否准确
        List<AreaModel> areaList = areaService.getAreaList();
        // 校验省市区
        // 省
        Optional<AreaModel> provinceAny = areaList.stream().filter(item -> request.getProvince().equals(item.getCode()))
                .findAny();
        if (!provinceAny.isPresent()) {
            throw new RuntimeException(MessageConstants.AREA_ERROR);
        }
        AreaModel province = provinceAny.get();

        // 市
        Optional<AreaModel> cityAny = areaList.stream().filter(item -> request.getCity().equals(item.getCode()))
                .findAny();
        if (!cityAny.isPresent()) {
            throw new RuntimeException(MessageConstants.AREA_ERROR);
        }
        AreaModel city = cityAny.get();
        // 区
        Optional<AreaModel> regionAny = areaList.stream().filter(item -> request.getRegion().equals(item.getCode()))
                .findAny();

        if (!regionAny.isPresent()) {
            throw new RuntimeException(MessageConstants.AREA_ERROR);
        }
        AreaModel region = regionAny.get();
        // 判断是否属于顺序的
        if (!city.getParentId().equals(province.getId())) {
            throw new RuntimeException(MessageConstants.AREA_ERROR);
        }
        if (!region.getParentId().equals(city.getId())) {
            throw new RuntimeException(MessageConstants.AREA_ERROR);
        }
        // 计费规则
        if (!rulesMapper.exists(Wrappers.<Rules>lambdaQuery().eq(Rules::getId, request.getRuleId()))) {
            throw new RuntimeException(MessageConstants.RULE_NOT_EXIST);
        }
        // 判断时间
        if (ObjectUtils.isNotEmpty(request.getPerationBeginTime())
                && ObjectUtils.isNotEmpty(request.getPerationEndTime())) {
            if (request.getPerationBeginTime().after(request.getPerationEndTime())) {
                throw new RuntimeException(MessageConstants.START_TIME_CANNOT_BE_LATER_THAN_END_TIME);
            }
        }
        // 即插即冲
        if (ObjectUtils.isNotEmpty(request.getPlugCharge())) {
            StationPlugAndPlayStatusEnum.exists(request.getPlugCharge());
        }
        // 是否优选
        if (ObjectUtils.isNotEmpty(request.getRecommend())) {
            StationPlugAndPlayStatusEnum.exists(request.getRecommend());
        }
        // 是否显示
        if (ObjectUtils.isNotEmpty(request.getShowStatus())) {
            StationPlugAndPlayStatusEnum.exists(request.getShowStatus());
        }
        // 场站状态
        if (ObjectUtils.isNotEmpty(request.getStatus())) {
            StationPlugAndPlayStatusEnum.exists(request.getStatus());
        }
    }

    /**
     * 保存场站附件
     *
     * @param request 保存的附件信息
     */
    private void saveStationAnnex(ChargingStationsAddRequest request, Long deptId) {
        Long stationId = request.getId();
        // 删除旧附件
        stationAnnexMapper
                .delete(Wrappers.<ChargingStationAnnex>lambdaQuery().eq(ChargingStationAnnex::getStationId, stationId));
        // logo
        if (StringUtils.isNotBlank(request.getLogoImage())) {
            stationAnnexMapper.insert(new ChargingStationAnnex(stationId, StationAnnexEnum.LOGO.getCode(),
                    request.getLogoImage(), deptId));
        }
        // 营业执照
        if (StringUtils.isNotBlank(request.getLicenseImage())) {
            stationAnnexMapper.insert(new ChargingStationAnnex(stationId, StationAnnexEnum.BUSINESS_LICENSE.getCode(),
                    request.getLicenseImage(), deptId));
        }
        // 场站
        if (CollectionUtils.isNotEmpty(request.getStationImageList())) {
            // 新增附件
            for (String imageUrl : request.getStationImageList()) {
                stationAnnexMapper.insert(new ChargingStationAnnex(stationId, StationAnnexEnum.STATION_IMAGE.getCode(),
                        imageUrl, deptId));
            }
        }
    }

}
