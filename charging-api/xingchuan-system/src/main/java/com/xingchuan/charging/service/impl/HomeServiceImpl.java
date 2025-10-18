package com.xingchuan.charging.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xingchuan.charging.domain.entity.ChargingOrder;
import com.xingchuan.charging.domain.entity.ChargingPile;
import com.xingchuan.charging.domain.entity.ChargingStations;
import com.xingchuan.charging.domain.model.ChargingRealtimeData;
import com.xingchuan.charging.domain.model.GunInfoModel;
import com.xingchuan.charging.domain.req.HomeBusinessTrendRequest;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.enums.OrderPayStatusEnum;
import com.xingchuan.charging.enums.OrderStatusEnum;
import com.xingchuan.charging.enums.TimeType;
import com.xingchuan.charging.mapper.ChargingGunsMapper;
import com.xingchuan.charging.mapper.ChargingOrderMapper;
import com.xingchuan.charging.mapper.ChargingPileMapper;
import com.xingchuan.charging.mapper.ChargingStationsMapper;
import com.xingchuan.charging.service.IHomeService;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页Service业务层处理
 */
@Service
public class HomeServiceImpl implements IHomeService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ChargingGunsMapper gunsMapper;
    @Resource
    private ChargingOrderMapper orderMapper;
    @Resource
    private ChargingStationsMapper stationsMapper;
    @Resource
    private ChargingPileMapper chargingPileMapper;
    @Autowired
    private ChargingOrderMapper chargingOrderMapper;

    /**
     * 处理订单信息
     *
     * @param response          返回参数
     * @param chargingOrderList 订单信息
     */
    private static void getOrderInfo(HomeBusinessTrendResponse response, List<ChargingOrder> chargingOrderList) {
        // 总充电量
        BigDecimal totalChargingAmount = BigDecimal.ZERO;
        // 订单总额
        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        // 充电服务费
        BigDecimal chargingServiceFee = BigDecimal.ZERO;

        if (CollectionUtils.isEmpty(chargingOrderList)) {
            return;
        }
        for (ChargingOrder chargingOrder : chargingOrderList) {
            totalOrderAmount = totalOrderAmount.add(chargingOrder.getTotalAmount());
            chargingServiceFee = chargingServiceFee.add(chargingOrder.getServiceFee());
            totalChargingAmount = totalChargingAmount.add(chargingOrder.getTotalPower());
        }
        response.setTotalChargingAmount(totalChargingAmount);
        response.setTotalOrderAmount(totalOrderAmount);
        response.setChargingServiceFee(chargingServiceFee);
    }

    /**
     * 首页-查询今日、昨日数据
     *
     * @param stationId 场站id
     * @return 结果
     */
    @Override
    public HomeDayChargingDataResponse getStationDayCharging(Long stationId) {
        HomeDayChargingDataResponse response = new HomeDayChargingDataResponse();
        // 查询场站
        ChargingStations chargingStations = stationsMapper.selectById(stationId);
        if (ObjectUtils.isEmpty(chargingStations)) {
            throw new IllegalArgumentException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        // 当日时间
        Date today = DateUtil.endOfDay(new Date());
        String todayTime = DateUtils.dateTime(today, DateUtils.YYYY_MM_DD);
        // 昨日时间
        Date yesterday = DateUtil.beginOfDay(DateUtils.addDays(today, -1));
        String yesterdayTime = DateUtils.dateTime(yesterday, DateUtils.YYYY_MM_DD);

        // 查询已经结束的充电订单
        List<ChargingOrder> chargingOrderList = orderMapper.selectList(Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getTotalPower, ChargingOrder::getTotalAmount, ChargingOrder::getServiceFee, ChargingOrder::getStartTime)
                .eq(ChargingOrder::getStationId, stationId)
                .between(ChargingOrder::getStartTime, yesterday, today)
        );

        if (CollectionUtils.isEmpty(chargingOrderList)) {
            return response;
        }

        Map<String, List<ChargingOrder>> chargingOrderMap = chargingOrderList.stream()
                .collect(Collectors.groupingBy(li -> DateUtils.dateTime(li.getStartTime())));

        // 当日时间数据
        List<ChargingOrder> todayOrderList = chargingOrderMap.get(todayTime);
        // 当日时间数据
        List<ChargingOrder> yesterdayOrderList = chargingOrderMap.get(yesterdayTime);

        // 订单数量
        int todayOrderQty = 0;
        int yesterdayOrderQty = 0;

        // 充电量（度）
        BigDecimal todayCharge = BigDecimal.ZERO;
        BigDecimal yesterdayCharge = BigDecimal.ZERO;
        // 充电订单总额
        BigDecimal todayOrderTotalAmount = BigDecimal.ZERO;
        BigDecimal yesterdayOrderTotalAmount = BigDecimal.ZERO;
        // 充电订单服务费
        BigDecimal todayOrderServiceFee = BigDecimal.ZERO;
        BigDecimal yesterdayOrderServiceFee = BigDecimal.ZERO;

        if (CollectionUtils.isNotEmpty(todayOrderList)) {
            todayOrderQty = todayOrderList.size();
            for (ChargingOrder chargingOrder : todayOrderList) {
                todayCharge = todayCharge.add(chargingOrder.getTotalPower());
                todayOrderServiceFee = todayOrderServiceFee.add(chargingOrder.getServiceFee());
                todayOrderTotalAmount = todayOrderTotalAmount.add(chargingOrder.getTotalAmount());
            }
        }
        response.setTodayOrderQty(todayOrderQty);

        response.setTodayCharge(todayCharge);
        response.setTodayOrderServiceFee(todayOrderServiceFee);
        response.setTodayOrderTotalAmount(todayOrderTotalAmount);

        if (CollectionUtils.isNotEmpty(yesterdayOrderList)) {
            yesterdayOrderQty = yesterdayOrderList.size();
            for (ChargingOrder chargingOrder : yesterdayOrderList) {
                yesterdayCharge = yesterdayCharge.add(chargingOrder.getTotalPower());
                yesterdayOrderServiceFee = yesterdayOrderServiceFee.add(chargingOrder.getServiceFee());
                yesterdayOrderTotalAmount = yesterdayOrderTotalAmount.add(chargingOrder.getTotalAmount());
            }
        }
        response.setYesterdayOrderQty(yesterdayOrderQty);

        response.setYesterdayCharge(yesterdayCharge);
        response.setYesterdayOrderServiceFee(yesterdayOrderServiceFee);
        response.setYesterdayOrderTotalAmount(yesterdayOrderTotalAmount);
        return response;
    }

    /**
     * 首页-获取场站实时状态数据
     *
     * @param stationId 场站id
     * @return 结果
     */
    @Override
    public HomeChargingPileStatusResponse getStationRealtimeStatusData(Long stationId) {
        // 查询场站，带权限查询判场站是否存在
        ChargingStations chargingStations = stationsMapper.selectOne(Wrappers.<ChargingStations>lambdaQuery()
                .select(ChargingStations::getOperatorId)
                .eq(ChargingStations::getId, stationId)
        );
        if (ObjectUtils.isEmpty(chargingStations)) {
            return new HomeChargingPileStatusResponse();
        }

        // 查询桩信息
        List<GunInfoModel> gunInfoList = chargingPileMapper.getGunNoByStationId(stationId);
        if (CollectionUtils.isEmpty(gunInfoList)) {
            return new HomeChargingPileStatusResponse();
        }

        // 桩数量
        int pileCount = gunInfoList.size();
        // 离线桩数量
        int offlinePileCount = 0;
        // 故障桩数量
        int faultyPileCount = 0;
        // 空闲桩数量
        int idlePileCount = 0;
        // 启动中桩数量
        int startingPileCount = 0;
        // 充电中桩数量
        int chargingPileCount = 0;
        // 查询redis 中数据
        for (GunInfoModel gunInfo : gunInfoList) {

            String redisKey = String.format(CacheConstants.DEVICE_STATUS_KEY, gunInfo.getDeviceNo(), gunInfo.getGunNo());
            // 获取redis中枪的状态
            String cacheObject = redisCache.getCacheObject(redisKey);

            if (ObjectUtils.isNotEmpty(cacheObject)) {
                ChargingRealtimeData orderProcessLog = JSON.parseObject(cacheObject, ChargingRealtimeData.class);

                switch (orderProcessLog.getStatus()) {
                    case OFFLINE:
                        offlinePileCount++;
                        break;
                    case FAULT:
                        faultyPileCount++;
                        break;
                    case ONLINE:
                        idlePileCount++;
                        break;
                    case STARTING:
                    case GUN_INSERTED:
                    case FINISHED:
                        startingPileCount++;
                        break;
                    case CHARGING:
                        chargingPileCount++;
                        break;
                }
            } else {
                offlinePileCount++;
            }
        }

        return new HomeChargingPileStatusResponse(pileCount, offlinePileCount, faultyPileCount, idlePileCount, startingPileCount, chargingPileCount);
    }

    /**
     * 获取场站经营趋势数据
     *
     * @param request 请求参数
     * @return 结果
     */
    @Override
    public List<HomeBusinessTrendResponse> getStationBusinessTrend(HomeBusinessTrendRequest request) {
        Date beginTime;
        Date endTime;

        Date date = new Date();
        int dateNumber = request.getDateNumber();
        TimeType timeType = request.getTimeType();
        // 处理时间
        if (TimeType.MONTH.equals(timeType)) {
            endTime = DateUtil.endOfMonth(date);
            beginTime = DateUtil.offsetMonth(DateUtil.beginOfMonth(date), dateNumber);
        } else {
            endTime = DateUtil.endOfDay(date);
            beginTime = DateUtil.offsetDay(DateUtil.beginOfDay(date), dateNumber);
        }
        List<HomeBusinessTrendResponse> responseList = getTimeProcessing(beginTime, endTime, timeType);

        List<ChargingOrder> chargingOrders = orderMapper.selectList(Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getStartTime, ChargingOrder::getTotalPower, ChargingOrder::getTotalAmount, ChargingOrder::getServiceFee)
                .eq(ChargingOrder::getStationId, request.getStationId())
                .between(ChargingOrder::getStartTime, beginTime, endTime)
        );

        if (CollectionUtils.isEmpty(chargingOrders)) {
            return responseList;
        }
        Map<String, List<ChargingOrder>> chargingOrderMap;
        if (TimeType.MONTH.equals(timeType)) {
            chargingOrderMap = chargingOrders.stream().peek(order -> order.setStartTime(DateUtil.beginOfMonth(order.getStartTime()))).collect(Collectors.groupingBy(li -> DateUtil.format(li.getStartTime(), "yyyy-MM-dd")));
        } else {
            chargingOrderMap = chargingOrders.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getStartTime(), "yyyy-MM-dd")));
        }

        BigDecimal percentage = BigDecimal.valueOf(100);

        for (int i = 0; i < responseList.size(); i++) {
            HomeBusinessTrendResponse response = responseList.get(i);

            List<ChargingOrder> chargingOrderList = chargingOrderMap.get(response.getTime());
            // 获取订单信息
            getOrderInfo(response, chargingOrderList);

            // 对比上一日期的参数
            if (i == 0) {
                continue;
            }
            HomeBusinessTrendResponse lastResponse = responseList.get(i - 1);
            // 总充电量对比
            BigDecimal totalChargingAmount = ObjectUtils.isEmpty(response.getTotalChargingAmount()) ? BigDecimal.ZERO : response.getTotalChargingAmount();
            BigDecimal lastTotalChargingAmount = ObjectUtils.isEmpty(lastResponse.getTotalChargingAmount()) ? BigDecimal.ZERO : lastResponse.getTotalChargingAmount();
            BigDecimal totalChargingAmountComparison = BigDecimal.ZERO;
            if (lastTotalChargingAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalChargingAmountComparison = totalChargingAmount.subtract(lastTotalChargingAmount)
                        .divide(lastTotalChargingAmount, 2, RoundingMode.HALF_UP).multiply(percentage);
            } else if (totalChargingAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalChargingAmountComparison = percentage;
            }
            response.setTotalChargingAmountComparison(totalChargingAmountComparison);

            // 订单总额对比
            BigDecimal totalOrderAmount = ObjectUtils.isEmpty(response.getTotalOrderAmount()) ? BigDecimal.ZERO : response.getTotalOrderAmount();
            BigDecimal lastTotalOrderAmount = ObjectUtils.isEmpty(lastResponse.getTotalOrderAmount()) ? BigDecimal.ZERO : lastResponse.getTotalOrderAmount();

            BigDecimal totalOrderAmountComparison = BigDecimal.ZERO;
            if (lastTotalOrderAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalOrderAmountComparison = totalOrderAmount.subtract(lastTotalOrderAmount)
                        .divide(lastTotalOrderAmount, 2, RoundingMode.HALF_UP).multiply(percentage);
            } else if (totalOrderAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalOrderAmountComparison = percentage;
            }
            response.setTotalOrderAmountComparison(totalOrderAmountComparison);

            // 充电服务费对比
            BigDecimal chargingServiceFee = ObjectUtils.isEmpty(response.getChargingServiceFee()) ? BigDecimal.ZERO : response.getChargingServiceFee();
            BigDecimal lastChargingServiceFee = ObjectUtils.isEmpty(lastResponse.getChargingServiceFee()) ? BigDecimal.ZERO : lastResponse.getChargingServiceFee();

            BigDecimal chargingServiceFeeComparison = BigDecimal.ZERO;
            if (lastChargingServiceFee.compareTo(BigDecimal.ZERO) > 0) {
                chargingServiceFeeComparison = chargingServiceFee.subtract(lastChargingServiceFee)
                        .divide(lastChargingServiceFee, 2, RoundingMode.HALF_UP).multiply(percentage);
            } else if (chargingServiceFee.compareTo(BigDecimal.ZERO) > 0) {
                chargingServiceFeeComparison = percentage;
            }
            response.setChargingServiceFeeComparison(chargingServiceFeeComparison);
        }

        return responseList;
    }

    /**
     * 首页-获取场站数据统计
     */
    @Override
    public StationDataStatisticsResponse getStationDataStatistics(Long stationId) {
        StationDataStatisticsResponse response = new StationDataStatisticsResponse();
        // 获取订单信息
        List<ChargingOrder> orderList = orderMapper.selectList(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getStationId, stationId));
        if (CollectionUtils.isEmpty(orderList)) {
            return response;
        }
        // 获取总耗电量
        BigDecimal totalPower = orderList.stream().map(ChargingOrder::getTotalPower).filter(ObjectUtils::isNotEmpty).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取订单总额
        BigDecimal totalAmount = orderList.stream().map(ChargingOrder::getTotalAmount).filter(ObjectUtils::isNotEmpty).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取用户数量
        int userQty = orderList.stream().map(ChargingOrder::getOpenId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toSet()).size();

        response.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        response.setTotalPower(totalPower.setScale(3, RoundingMode.HALF_UP));
        response.setUserQty(userQty);
        return response;
    }


    @Override
    public DashboardOperatorInfoResponse getDashboardOperatorInfo() {
        // 查询电站数量
        Long stationCount = stationsMapper.selectCount(Wrappers.<ChargingStations>lambdaQuery()
                .eq(ChargingStations::getShowStatus, 1)
        );
        // 查询终端数量
        Long terminalCount = gunsMapper.selectCount(Wrappers.lambdaQuery());

        // 查询充电桩数量
        Long pileCount = chargingPileMapper.selectCount(Wrappers.<ChargingPile>lambdaQuery().eq(ChargingPile::getPileStatus, 1));

        return DashboardOperatorInfoResponse.builder()
                .pileCount(Math.toIntExact(pileCount))
                .stationCount(Math.toIntExact(stationCount))
                .terminalCount(Math.toIntExact(terminalCount))
                .build();
    }

    @Override
    public DashboardOperationDataResponse queryOperationData() {
        List<ChargingOrder> dataList = chargingOrderMapper.selectList(Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getEndTime, ChargingOrder::getTotalPower, ChargingOrder::getRealAmount)
                .eq(ChargingOrder::getOrderState, OrderStatusEnum.BILL_HAS_BEEN_UPLOADED.getCode())
                .eq(ChargingOrder::getPayStatus, OrderPayStatusEnum.PAY_SUCCESS.getCode())
                .isNotNull(ChargingOrder::getEndTime)
                .le(ChargingOrder::getCreateTime, LocalDateTime.now())
                .ge(ChargingOrder::getCreateTime, LocalDate.of(LocalDate.now().getYear(), 1, 1).atStartOfDay())
        );
        if (dataList.isEmpty()) {
            return new DashboardOperationDataResponse();
        }
        // 年度充电量
        BigDecimal annualChargingVolume = BigDecimal.ZERO;
        // 年度充电次数
        int annualChargingCount = 0;
        // 年营收
        BigDecimal annualRevenue = BigDecimal.ZERO;
        // 月度充电次数
        int monthlyChargingCount = 0;
        // 月度充电量
        BigDecimal monthlyChargingVolume = BigDecimal.ZERO;
        // 月度营收
        BigDecimal monthlyRevenue = BigDecimal.ZERO;
        // 今日充电次数
        int todayChargingCount = 0;
        // 今日充电量
        BigDecimal todayChargingVolume = BigDecimal.ZERO;
        // 今日营收
        BigDecimal todayRevenue = BigDecimal.ZERO;

        for (ChargingOrder data : dataList) {
            // 月度
            LocalDate localDate = DateUtils.toLocalDate(data.getEndTime());

            if (localDate != null && localDate.getMonthValue() == LocalDate.now().getMonthValue()) {
                monthlyChargingVolume = monthlyChargingVolume.add(data.getTotalPower());
                monthlyChargingCount += 1;
                monthlyRevenue = monthlyRevenue.add(data.getRealAmount());
            }
            // 今日
            if (localDate != null && localDate.equals(LocalDate.now())) {
                todayChargingVolume = todayChargingVolume.add(data.getTotalPower());
                todayChargingCount += 1;
                todayRevenue = todayRevenue.add(data.getRealAmount());
            }

            // 年度
            annualChargingVolume = annualChargingVolume.add(data.getTotalPower());
            annualChargingCount += 1;
            annualRevenue = annualRevenue.add(data.getRealAmount());
        }

        DashboardOperationDataResponse response = new DashboardOperationDataResponse();
        response.setAnnualChargingVolume(annualChargingVolume);
        response.setAnnualChargingCount(annualChargingCount);
        response.setAnnualRevenue(annualRevenue);
        response.setMonthlyChargingCount(monthlyChargingCount);
        response.setMonthlyChargingVolume(monthlyChargingVolume);
        response.setMonthlyRevenue(monthlyRevenue);
        response.setTodayChargingCount(todayChargingCount);
        response.setTodayChargingVolume(todayChargingVolume);
        response.setTodayRevenue(todayRevenue);

        return response;
    }

    /**
     * 处理时间
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @return 结果
     */
    private List<HomeBusinessTrendResponse> getTimeProcessing(Date beginTime, Date endTime, TimeType timeType) {
        List<HomeBusinessTrendResponse> responseList = new ArrayList<>();
        while (beginTime.before(endTime)) {
            HomeBusinessTrendResponse response = new HomeBusinessTrendResponse();

            response.setTime(DateUtil.format(beginTime, "yyyy-MM-dd"));
            if (TimeType.MONTH.equals(timeType)) {
                beginTime = DateUtil.offsetMonth(beginTime, 1);
            } else {
                beginTime = DateUtil.offsetDay(beginTime, 1);
            }
            responseList.add(response);
        }
        return responseList;
    }

}
