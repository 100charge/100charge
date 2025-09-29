package com.xingchuan.charging.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xingchuan.charging.domain.entity.*;
import com.xingchuan.charging.domain.model.ChargingRealtimeData;
import com.xingchuan.charging.domain.model.DeviceCMDMsg;
import com.xingchuan.charging.domain.model.GunInfoModel;
import com.xingchuan.charging.domain.model.StationChargingLimitModel;
import com.xingchuan.charging.domain.req.OrderSettlementRequest;
import com.xingchuan.charging.domain.req.StartChargingRequest;
import com.xingchuan.charging.domain.resp.ChargingRealtimeDataStatistics;
import com.xingchuan.charging.enums.*;
import com.xingchuan.charging.mapper.*;
import com.xingchuan.charging.mq.producer.DeviceProducer;
import com.xingchuan.charging.service.IChargingService;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.OrderNoUtils;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.system.service.ISysConfigService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ChargingServiceImpl implements IChargingService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ChargingPileMapper pileMapper;
    @Resource
    private ChargingGunsMapper gunsMapper;
    @Resource
    private DeviceProducer deviceProducer;
    @Resource
    private ChargingOrderMapper orderMapper;
    @Resource
    private AppUserBalanceMapper balanceMapper;
    @Resource
    private ISysConfigService sysConfigService;
    @Resource
    private ChargingStationsMapper stationsMapper;
    @Resource
    private AppUserBalanceRecordMapper balanceRecordMapper;


    /**
     * 获取桩的实时状态
     */
    @Override
    public ChargingRealtimeDataStatistics getRealTimeStatus(Long stationId) {
        ChargingRealtimeDataStatistics response = new ChargingRealtimeDataStatistics();

        ChargingStations stations = stationsMapper.selectById(stationId);
        if (ObjectUtils.isEmpty(stations)) {
            return response;
        }
        //根据场站id查询桩编码和枪编码
        List<GunInfoModel> gunInfoList = pileMapper.getGunNoByStationId(stations.getId());
        if (ObjectUtils.isEmpty(gunInfoList)) {
            return response;
        }
        List<ChargingRealtimeData> responseList = new ArrayList<>();

        for (GunInfoModel gunInfo : gunInfoList) {

            String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, gunInfo.getDeviceNo(), gunInfo.getGunNo());
            // 获取redis中枪的状态
            String cacheObject = redisCache.getCacheObject(redisKey);

            if (ObjectUtils.isNotEmpty(cacheObject)) {
                ChargingRealtimeData orderProcessLog = JSON.parseObject(cacheObject, ChargingRealtimeData.class);
                orderProcessLog.setPileId(gunInfo.getPileId());
                responseList.add(orderProcessLog);
            } else {
                ChargingRealtimeData orderProcessLog = new ChargingRealtimeData();
                orderProcessLog.setPileId(gunInfo.getPileId());
                orderProcessLog.setDeviceNo(gunInfo.getDeviceNo());
                orderProcessLog.setGunNo(gunInfo.getGunNo());
                orderProcessLog.setStatus(ChargeGunsEnum.OFFLINE);
                responseList.add(orderProcessLog);
            }
        }

        response.setResponseList(responseList);

        int total = responseList.size();
        // 获取实时状态统计
        getResponse(response, responseList, total);

        return response;
    }

    /**
     * 开始充电
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startCharging(StartChargingRequest request) {
        Long stationId = request.getChargingId();
        String deviceNo = request.getDeviceNo();
        String gunsNo = request.getGunsNo();
        AppUser appUser = SecurityUtils.getAppUser();
        String userOpenId = appUser.getOpenId();
        Integer optionType = request.getOptionType().getCode();
        String plateNo = request.getPlateNo();
        // 校验场站是否存在
        ChargingStations chargingStations = getStartChargingStation(stationId);
        if (ObjectUtils.isEmpty(chargingStations)) {
            throw new ServiceException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        // 场站是否启用
        if (!chargingStations.getStatus().equals(ChargingStationsEnum.OPEN.getCode())) {
            throw new ServiceException(MessageConstants.STATION_CLOSED);
        }
        //判断是否存在待支付订单
        boolean existed = orderMapper.exists(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getOpenId, userOpenId)
                .ne(ChargingOrder::getOrderState, OrderStatusEnum.ABNORMAL.getCode())
                .ne(ChargingOrder::getPayStatus, OrderPayStatusEnum.PAY_SUCCESS.getCode()));
        if (existed) {
            throw new ServiceException(MessageConstants.EXIST_PENDING_PAYMENT_ORDERS);
        }

        ChargingOrder chargingOrder = new ChargingOrder();
        // 停止金额
        BigDecimal limitAmount = chargingStations.getChargingLimitAmount();
        // 小程序用户校验+余额
        BigDecimal balance = checkAppUserStartCharging(userOpenId);
        // 校验充电桩、枪位是否正确
        ChargingPile chargingPile = checkChargingDeviceStartCharging(stationId, deviceNo, gunsNo);
        // 获取redis中枪的状态
        validateGunStatusInRedis(deviceNo, gunsNo);
        // 创建充电订单
        // 1. 生成订单号
        int protocolVersion = chargingPile.getProtocolVersion();
        // 2. 协议版本号
        ProtocolTypeEnum enumByCode = ProtocolTypeEnum.getEnumByCode(protocolVersion);
        String orderNo = getChargingOrderNoByProtocolType(enumByCode, deviceNo, gunsNo, optionType);
        OrderPayTypeEnum payType = request.getPayType();

        chargingOrder.setGunNo(gunsNo);
        chargingOrder.setTradeNo(orderNo);
        chargingOrder.setPlateNo(plateNo);
        chargingOrder.setDeviceNo(deviceNo);
        chargingOrder.setOpenId(userOpenId);
        chargingOrder.setStationId(stationId);
        chargingOrder.setStartTime(new Date());
        chargingOrder.setOrderSource(optionType);
        chargingOrder.setPayment(payType.getCode());
        chargingOrder.setDeptId(appUser.getDeptId());
        chargingOrder.setMobile(appUser.getPhoneNumber());
        chargingOrder.setStationName(chargingStations.getName());
        chargingOrder.setOrderState(OrderStatusEnum.NO_START.getCode());
        chargingOrder.setPayStatus(OrderPayStatusEnum.NOT_PAY.getCode());
        // 插入订单
        addChargingOrder(chargingOrder);
        // 缓存订单信息致redis，方法
        cacheOrderInfoInRedis(chargingOrder, limitAmount, protocolVersion, balance);
        // 过mq 通知协议那边启动充电
        String timeMillis = String.valueOf(System.currentTimeMillis());
        DeviceCMDMsg msg = new DeviceCMDMsg(timeMillis, deviceNo, orderNo, gunsNo, CMDEnums.START, enumByCode);
        SendResult result = deviceProducer.sendDelayMsg(msg);
        if (ObjectUtils.isEmpty(result) || !SendStatus.SEND_OK.equals(result.getSendStatus())) {
            throw new ServiceException(MessageConstants.SEND_CMD_MSG_ERROR);
        }

        return orderNo;
    }

    /**
     * 结束充电
     *
     * @param tradeNo 订单编号
     */
    @Override
    public String endCharge(String tradeNo) {
        // 查询订单是否存在
        ChargingOrder chargingOrder = orderMapper.selectOne(Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getOrderState, ChargingOrder::getDeviceNo, ChargingOrder::getGunNo,
                        ChargingOrder::getTradeNo, ChargingOrder::getStartTime, ChargingOrder::getStationName,
                        ChargingOrder::getOpenId)
                .eq(ChargingOrder::getTradeNo, tradeNo)
        );
        if (ObjectUtils.isEmpty(chargingOrder)) {
            return MessageConstants.ORDER_NOT_EXIST;
        }
        Integer orderState = chargingOrder.getOrderState();

        // 查询订单当前状态
        if (OrderStatusEnum.NO_START.getCode().equals(orderState)) {
            return MessageConstants.ORDER_NOT_STARTED;
        }
        if (!OrderStatusEnum.CHARGING.getCode().equals(orderState)) {
            return MessageConstants.ORDER_ENDED_OR_EXCEPTIONAL;
        }
        // 启动充电未超过90s不允许停止
        Date startTime = chargingOrder.getStartTime();
        long currentTime = System.currentTimeMillis();

        Duration duration = Duration.between(startTime.toInstant(), Instant.ofEpochMilli(currentTime));
        if (duration.toMillis() < 90 * 1000) {
            return MessageConstants.ERROR_CHARGING_TIME_LIMIT_NOT_EXCEEDED;
        }

        // 过mq 通知协议那边停止充电
        String deviceNo = chargingOrder.getDeviceNo();
        String gunsNo = chargingOrder.getGunNo();
        // 获取协议号
        ChargingPile chargingPile = pileMapper.selectOne(Wrappers.<ChargingPile>lambdaQuery().select(ChargingPile::getProtocolVersion)
                .eq(ChargingPile::getDeviceNo, deviceNo));
        ProtocolTypeEnum enumByCode = ProtocolTypeEnum.getEnumByCode(chargingPile.getProtocolVersion());

        String timeMillis = String.valueOf(System.currentTimeMillis());
        DeviceCMDMsg msg = new DeviceCMDMsg(timeMillis, deviceNo, tradeNo, gunsNo, CMDEnums.STOP, enumByCode);
        SendResult result = deviceProducer.sendDelayMsg(msg);
        if (ObjectUtils.isEmpty(result) || !SendStatus.SEND_OK.equals(result.getSendStatus())) {
            return MessageConstants.SEND_CMD_MSG_ERROR;
        }
        return StringUtil.EMPTY_STRING;
    }

    /**
     * 订单支付
     *
     * @param request 请求参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayment(OrderSettlementRequest request) {
        // 当前用户openId
        AppUser appUser = SecurityUtils.getAppUser();
        String userOpenId = appUser.getOpenId();
        // 查询订单
        String tradeNo = request.getTradeNo();
        // 优惠券id
        Long promotionId = request.getPromotionId();
        // 查询订单
        ChargingOrder chargingOrder = orderMapper.selectOne(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getTradeNo, tradeNo).eq(ChargingOrder::getOpenId, userOpenId));
        if (ObjectUtils.isEmpty(chargingOrder)) {
            throw new ServiceException(MessageConstants.ORDER_NOT_EXIST);
        }
        if (ObjectUtils.isEmpty(chargingOrder.getRealAmount())) {
            chargingOrder.setRealAmount(chargingOrder.getTotalAmount());
        }

        // 自动支付
        autoPay(chargingOrder, promotionId, appUser);
    }

    /**
     * 获取实时状态统计
     */
    private void getResponse(ChargingRealtimeDataStatistics response, List<ChargingRealtimeData> responseList, int total) {
        int online = (int) responseList.stream().filter(item -> ChargeGunsEnum.ONLINE.equals(item.getStatus())).count();
        int charging = (int) responseList.stream().filter(item -> ChargeGunsEnum.CHARGING.equals(item.getStatus())).count();
        int starting = (int) responseList.stream().filter(item -> ChargeGunsEnum.STARTING.equals(item.getStatus())).count();
        int gunInserted = (int) responseList.stream().filter(item -> ChargeGunsEnum.GUN_INSERTED.equals(item.getStatus())).count();
        int offline = (int) responseList.stream().filter(item -> ChargeGunsEnum.OFFLINE.equals(item.getStatus())).count();
        int fault = (int) responseList.stream().filter(item -> ChargeGunsEnum.FAULT.equals(item.getStatus())).count();
        int finished = (int) responseList.stream().filter(item -> ChargeGunsEnum.FINISHED.equals(item.getStatus())).count();

        response.setTotal(total);
        response.setOnline(online);
        response.setCharging(charging);
        response.setStarting(starting);
        response.setGunInserted(gunInserted);
        response.setOffline(offline);
        response.setFault(fault);
        response.setFinished(finished);
    }

    /**
     * 获取启动充电的场站信息
     *
     * @param stationId id
     * @return 结果
     */
    public ChargingStations getStartChargingStation(Long stationId) {
        return stationsMapper.selectOne(Wrappers.<ChargingStations>lambdaQuery()
                .select(ChargingStations::getId,
                        ChargingStations::getName,
                        ChargingStations::getStatus,
                        ChargingStations::getTenantId,
                        ChargingStations::getOperatorId,
                        ChargingStations::getDeptId,
                        ChargingStations::getChargingLimitAmount)
                .eq(ChargingStations::getId, stationId)
        );
    }

    /**
     * 校验小程序用户是否能启动充电
     *
     * @param userOpenId 用户标识
     */
    private BigDecimal checkAppUserStartCharging(String userOpenId) {
        // 判断用户余额是否达到最低充电金额
        AppUserBalance userBalance = balanceMapper.selectOne(Wrappers.<AppUserBalance>lambdaQuery().eq(AppUserBalance::getOpenId, userOpenId));
        String minimumRechargeAmountStr = sysConfigService.selectConfigByKey("mini_app_min_charge_amount");

        BigDecimal balance = userBalance.getBalance();
        BigDecimal minimumRechargeAmount = StringUtils.isBlank(minimumRechargeAmountStr) ? BigDecimal.valueOf(2) : new BigDecimal(minimumRechargeAmountStr);

        if (minimumRechargeAmount.compareTo(balance) > 0) {
            throw new ServiceException(MessageConstants.USER_BALANCE_NOT_ENOUGH);
        }
        return balance;
    }

    /**
     * 校验充电设备是否能启动充电
     *
     * @param stationId 场站id
     * @param deviceNo  充电桩号
     * @param gunsNo    充电枪号
     * @return 充电枪
     */
    private ChargingPile checkChargingDeviceStartCharging(Long stationId, String deviceNo, String gunsNo) {
        ChargingPile chargingPile = pileMapper.selectOne(Wrappers.<ChargingPile>lambdaQuery().eq(ChargingPile::getStationId, stationId)
                .eq(ChargingPile::getDeviceNo, deviceNo).last("LIMIT 1"));
        if (ObjectUtils.isEmpty(chargingPile)) {
            throw new ServiceException(MessageConstants.CHARGING_PILE_NOT_EXIST);
        }
        // 判断充电桩是否启用
        if (!chargingPile.getPileStatus().equals(PileStatusEnum.ENABLE.getCode())) {
            throw new ServiceException(MessageConstants.CHARGER_DISABLED);
        }

        boolean chargingGunsExists = gunsMapper.exists(Wrappers.<ChargingGuns>lambdaQuery().eq(ChargingGuns::getDeviceNo, deviceNo)
                .eq(ChargingGuns::getNo, gunsNo));
        if (!chargingGunsExists) {
            throw new ServiceException(MessageConstants.CHARGING_GUN_NOT_EXIST);
        }
        return chargingPile;
    }

    /**
     * 校验redis中枪的状态，判断能否充电
     */
    private void validateGunStatusInRedis(String deviceNo, String gunsNo) {
        String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, deviceNo, gunsNo);
        String cacheObject = redisCache.getCacheObject(redisKey);
        if (ObjectUtils.isNotEmpty(cacheObject)) {
            ChargingRealtimeData orderProcessLog = com.alibaba.fastjson.JSON.parseObject(cacheObject, ChargingRealtimeData.class);
            ChargeGunsEnum processLogStatus = orderProcessLog.getStatus();
            // 判断枪状态
            if (!ChargeGunsEnum.ONLINE.equals(processLogStatus) && !ChargeGunsEnum.GUN_INSERTED.equals(processLogStatus)) {
                // 当前充电枪不可用
                throw new RuntimeException(MessageConstants.CURRENT_CHARGING_GUN_UNAVAILABLE);
            }
            if (!orderProcessLog.getGunInserted()) {
                throw new RuntimeException(MessageConstants.DEVICE_NOT_PLUGGED_IN);
            }
        } else {
            log.error("redis中枪的状态为空");
            throw new RuntimeException(MessageConstants.CURRENT_CHARGING_GUN_UNAVAILABLE);
        }
    }

    /**
     * 生成订单号
     *
     * @param typeEnum   协议类型
     * @param deviceNo   桩号
     * @param gunNo      枪号
     * @param optionType 订单来源
     * @return 订单号
     */
    private String getChargingOrderNoByProtocolType(ProtocolTypeEnum typeEnum, String deviceNo, String gunNo, Integer optionType) {
        String source = Objects.equals(OrderSourceEnum.WECHAT.getCode(), optionType) ? TradeNoSourceEnum.WE_CHAT.getCode() : TradeNoSourceEnum.ALI_PAT.getCode();

        switch (typeEnum) {
            case TELD_AC:
                return OrderNoUtils.generateTELDACOrderNo(deviceNo, gunNo, source);
            case TELD_DC:
                return OrderNoUtils.generateTELDDCOrderNo(deviceNo, gunNo, source);
            case HLHT:
                return OrderNoUtils.generateHLHTOrderNo(deviceNo, gunNo, source);
            default:
                return OrderNoUtils.generateYKCOrderNo(deviceNo, gunNo, source);
        }
    }

    /**
     * 新增充电订单
     *
     * @param chargingOrder 订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void addChargingOrder(ChargingOrder chargingOrder) {
        if (orderMapper.insert(chargingOrder) == 0) {
            throw new ServiceException(MessageConstants.CHARGING_START_FAILED);
        }
    }

    /**
     * 缓存订单信息
     *
     * @param chargingOrder   订单信息
     * @param limitAmount     限额
     * @param protocolVersion 协议版本
     * @param balance         余额
     */
    private void cacheOrderInfoInRedis(ChargingOrder chargingOrder, BigDecimal limitAmount, int protocolVersion, BigDecimal balance) {
        StationChargingLimitModel chargingLimitModel = new StationChargingLimitModel(chargingOrder, limitAmount, protocolVersion, balance);
        String redisKey = String.format(CacheConstants.STATION_CHARGING_LIMIT_KEY, chargingOrder.getTradeNo());
        // 缓存两小时
        redisCache.setCacheObject(redisKey, JSONObject.toJSONString(chargingLimitModel), 2, TimeUnit.HOURS);
    }

    /**
     * 自动支付
     *
     * @param order       订单信息
     * @param promotionId 优惠券id
     * @param appUser     用户信息
     */
    private void autoPay(ChargingOrder order, Long promotionId, AppUser appUser) {
        // 用户openId
        String openId = order.getOpenId();
        // 订单号
        String tradeNo = order.getTradeNo();
        // 支付状态
        Integer payStatus = order.getPayStatus();
        // 判断订单状态
        if (!OrderStatusEnum.BILL_HAS_BEEN_UPLOADED.getCode().equals(order.getOrderState())) {
            throw new ServiceException(MessageConstants.ORDER_NOT_COMPLETE_OR_EXCEPTION);
        }
        // 判断订单支付状态
        if (!OrderPayStatusEnum.NOT_PAY.getCode().equals(payStatus)) {
            throw new ServiceException(MessageConstants.ORDER_PAYMENT_STATUS_EXCEPTION);
        }
        // 获取场站名称
        String stationName = stationsMapper.selectOne(Wrappers.<ChargingStations>lambdaQuery()
                .select(ChargingStations::getName).eq(ChargingStations::getId, order.getStationId())).getName();
        // 实际应付金额
        BigDecimal realAmount = order.getRealAmount();
        //小程序用户订单支付
        appUserPayment(openId, realAmount, order, tradeNo, stationName);
    }

    /**
     * 小程序用户订单支付
     */
    private void appUserPayment(String userOpenId, BigDecimal realAmount, ChargingOrder chargingOrder, String tradeNo, String stationName) {
        // 判断余额、判断是否够扣的
        AppUserBalance userBalance = balanceMapper.selectOne(Wrappers.<AppUserBalance>lambdaQuery()
                .eq(AppUserBalance::getOpenId, userOpenId));
        if (ObjectUtils.isEmpty(userBalance) || userBalance.getBalance().compareTo(realAmount) < 0) {
            throw new ServiceException(MessageConstants.USER_BALANCE_NOT_ENOUGH);
        }
        // 修改订单状态
        chargingOrder.setPayTime(new Date());
        chargingOrder.setPayment(OrderPayTypeEnum.BALANCE.getCode());
        chargingOrder.setPayStatus(OrderPayStatusEnum.PAY_SUCCESS.getCode());
        orderMapper.updateById(chargingOrder);
        // 扣除余额
        BigDecimal balanceAmount = userBalance.getBalance().subtract(realAmount);
        balanceMapper.update(Wrappers.<AppUserBalance>lambdaUpdate().set(AppUserBalance::getBalance, balanceAmount)
                .eq(AppUserBalance::getOpenId, userOpenId));

        // 创建支付记录
        AppUserBalanceRecord balanceRecord = new AppUserBalanceRecord();
        balanceRecord.setTradeNo(tradeNo);
        balanceRecord.setOpenId(userOpenId);
        balanceRecord.setStationName(stationName);
        balanceRecord.setType(AppUserBalanceRecordEnum.CONSUME.getCode());

        if (ObjectUtils.isNotEmpty(chargingOrder.getOutTradeNo())) {
            balanceRecord.setOutTradeNo(chargingOrder.getOutTradeNo());
        }
        balanceRecord.setAmount(realAmount);
        balanceRecord.setLastAmount(balanceAmount);

        balanceRecordMapper.insert(balanceRecord);
    }

}
