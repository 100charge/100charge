package com.xingchuan.charging.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.ChargingOrder;
import com.xingchuan.charging.domain.entity.ChargingStationReviews;
import com.xingchuan.charging.domain.excel.ChargingOrderExport;
import com.xingchuan.charging.domain.model.ChargingRealtimeDataModel;
import com.xingchuan.charging.domain.req.ChargingOrderPageListRequest;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.enums.*;
import com.xingchuan.charging.mapper.AppUserMapper;
import com.xingchuan.charging.mapper.ChargingOrderMapper;
import com.xingchuan.charging.mapper.ChargingStationReviewsMapper;
import com.xingchuan.charging.mq.OrderBillProducer;
import com.xingchuan.charging.service.IChargingOrderService;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.DateUtils;
import com.xingchuan.common.utils.PageUtils;
import com.xingchuan.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 充电订单Service业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class ChargingOrderServiceImpl extends ServiceImpl<ChargingOrderMapper, ChargingOrder> implements IChargingOrderService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private AppUserMapper appUserMapper;
    @Resource
    private OrderBillProducer orderBillProducer;
    @Resource
    private ChargingStationReviewsMapper stationReviewsMapper;

    /**
     * 小程序获取订单查询条件
     */
    private static LambdaQueryWrapper<ChargingOrder> getChargingOrderLambdaQueryWrapper(Integer status) {
        String userOpenId = SecurityUtils.getUserOpenId();

        LambdaQueryWrapper<ChargingOrder> queryWrapper = Wrappers.lambdaQuery();
        if (ObjectUtils.isNotEmpty(status)) {
            //进行中
            if (status.equals(MiniStatusEnum.UNDERWAY.getCode())) {
                queryWrapper.in(ChargingOrder::getOrderState, OrderStatusEnum.CHARGING.getCode(),
                        OrderStatusEnum.GUN_NOT_DRAWN.getCode(), OrderStatusEnum.CHARGING_COMPLETED.getCode());
                //待支付
            } else if (status.equals(MiniStatusEnum.PENDING_PAY.getCode())) {
                queryWrapper.eq(ChargingOrder::getOrderState, OrderStatusEnum.BILL_HAS_BEEN_UPLOADED.getCode())
                        .eq(ChargingOrder::getPayStatus, OrderPayStatusEnum.NOT_PAY.getCode());
                //已完成
            } else if (status.equals(MiniStatusEnum.COMPLETE.getCode())) {
                queryWrapper.eq(ChargingOrder::getPayStatus, OrderPayStatusEnum.PAY_SUCCESS.getCode());
            } else {
                throw new ServiceException(MessageConstants.PARAM_ERROR);
            }
        }
        queryWrapper.ne(ChargingOrder::getOrderState, OrderStatusEnum.ABNORMAL.getCode());
        queryWrapper.eq(ChargingOrder::getOpenId, userOpenId);
        queryWrapper.orderByDesc(ChargingOrder::getStartTime);
        return queryWrapper;
    }

    /**
     * 充电订单列表分页查询
     */
    @Override
    public Page<ChargingOrderPageListResponse> selectPageList(ChargingOrderPageListRequest request) {
        Page<ChargingOrder> pageInfo = PageUtils.getPageInfo(ChargingOrder.class);
        Page<ChargingOrderPageListResponse> responsePage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());

        // 构造查询条件
        LambdaQueryWrapper<ChargingOrder> orderQueryWrapper = getOrderQueryWrapper(request);
        Page<ChargingOrder> chargingOrderPage = baseMapper.selectPage(pageInfo, orderQueryWrapper);

        if (ObjectUtils.isNotEmpty(chargingOrderPage.getRecords())) {
            List<ChargingOrderPageListResponse> responseList = new ArrayList<>();
            for (ChargingOrder record : chargingOrderPage.getRecords()) {
                ChargingOrderPageListResponse response = new ChargingOrderPageListResponse();
                BeanUtils.copyProperties(record, response);
                response.setOrderStateDesc(OrderStatusEnum.getDescByCode(record.getOrderState()));
                response.setPayStatusDesc(OrderPayStatusEnum.getDescByCode(record.getPayStatus()));
                //判断订单是否正在充电
                if (OrderStatusEnum.CHARGING.getCode().equals(record.getOrderState())) {
                    //获取实时数据
                    getOrderRealTimeInfo(record, response);
                }
                responseList.add(response);
            }
            responsePage.setRecords(responseList);
            responsePage.setTotal(chargingOrderPage.getTotal());
        }
        return responsePage;
    }

    /**
     * 充电订单统计
     */
    @Override
    public OrderStatisticsResponse orderStatistics(ChargingOrderPageListRequest request) {
        OrderStatisticsResponse response = new OrderStatisticsResponse();
        // 构造查询条件
        LambdaQueryWrapper<ChargingOrder> orderQueryWrapper = getOrderQueryWrapper(request);

        // 查询订单
        List<ChargingOrder> orderList = baseMapper.selectList(orderQueryWrapper);
        // 数据统计
        getOrderStatisticsData(orderList, response);
        return response;
    }

    /**
     * 充电订单详情查询
     */
    @Override
    public ChargingOrderDetailResponse orderDetail(Long id) {
        ChargingOrderDetailResponse response = new ChargingOrderDetailResponse();
        ChargingOrder order = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(order)) {
            return response;
        }
        BeanUtils.copyProperties(order, response);

        response.setOrderStateDesc(OrderStatusEnum.getDescByCode(order.getOrderState()));
        response.setPaymentDesc(OrderPayTypeEnum.getDescByCode(order.getPayment()));
        response.setOrderSourceDesc(OrderSourceEnum.getDescByCode(order.getOrderSource()));
        response.setPayStatusDesc(OrderPayStatusEnum.getDescByCode(order.getPayStatus()));
        response.setUserName(order.getMobile());

        if (ObjectUtils.isNotEmpty(order.getStartTime()) && ObjectUtils.isNotEmpty(order.getEndTime())) {
            response.setRealDuration(DateUtils.getDatePoor(order.getEndTime(), order.getStartTime()));
        }

        return response;
    }

    /**
     * 充电订单列表导出
     */
    @Override
    public List<ChargingOrderExport> export(ChargingOrderPageListRequest request) {
        List<ChargingOrderExport> responseList = new ArrayList<>();
        // 构造查询条件
        LambdaQueryWrapper<ChargingOrder> orderQueryWrapper = getOrderQueryWrapper(request);
        // 获取订单列表
        List<ChargingOrder> orderList = baseMapper.selectList(orderQueryWrapper);

        if (ObjectUtils.isNotEmpty(orderList)) {

            for (ChargingOrder record : orderList) {
                ChargingOrderExport response = new ChargingOrderExport();
                BeanUtils.copyProperties(record, response);
                responseList.add(response);
            }
        }
        return responseList;
    }

    /**
     * 未支付的订单进行主动扣款
     */
    @Override
    public void autoPayOrder() {
        List<ChargingOrder> chargingOrderList = baseMapper.selectList(Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getTradeNo)
                .eq(ChargingOrder::getPayStatus, OrderPayStatusEnum.NOT_PAY.getCode())
                .eq(ChargingOrder::getOrderState, OrderStatusEnum.BILL_HAS_BEEN_UPLOADED.getCode())
        );
        if (chargingOrderList.isEmpty()) {
            log.info("未找到待扣费的订单");
            return;
        }
        for (ChargingOrder chargingOrder : chargingOrderList) {
            orderBillProducer.sendBill(chargingOrder.getTradeNo());
        }
    }

    /**
     * 置为异常订单
     *
     * @param tradeNo 订单号
     */
    @Override
    public void markOrderAsAbnormal(String tradeNo) {
        List<Integer> orderStateList = Arrays.asList(
                // 未开始
                OrderStatusEnum.NO_START.getCode(),
                // 充电中
                OrderStatusEnum.CHARGING.getCode(),
                // 充电结束未拔枪
                OrderStatusEnum.GUN_NOT_DRAWN.getCode()
        );
        // 充电金额和充电量不能为0
        boolean exists = baseMapper.exists(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getTradeNo, tradeNo)
                .eq(ChargingOrder::getTotalAmount, BigDecimal.ZERO)
                .eq(ChargingOrder::getTotalPower, BigDecimal.ZERO)
                .in(ChargingOrder::getOrderState, orderStateList)
        );
        if (!exists) {
            throw new RuntimeException("订单不存在或当前状态不允许修改");
        }
        baseMapper.update(Wrappers.<ChargingOrder>lambdaUpdate()
                .set(ChargingOrder::getOrderState, OrderStatusEnum.ABNORMAL.getCode())
                .set(ChargingOrder::getStopReason, "订单异常，后台手动停止")
                .eq(ChargingOrder::getTradeNo, tradeNo)
        );
    }

    /**
     * 小程序-充电订单列表分页查询
     */
    @Override
    public Page<OrderPageListResponse> miniOrderList(Integer status) {
        Page<OrderPageListResponse> responsePage = new Page<>();

        Page<Object> pageInfo = PageUtils.getPageInfo();
        Page<ChargingOrder> page = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());
        LambdaQueryWrapper<ChargingOrder> queryWrapper = getChargingOrderLambdaQueryWrapper(status);

        Page<ChargingOrder> orderPage = baseMapper.selectPage(page, queryWrapper);
        if (orderPage.getTotal() <= 0) {
            return responsePage;
        }

        List<OrderPageListResponse> responseList = new ArrayList<>();
        for (ChargingOrder record : orderPage.getRecords()) {
            OrderPageListResponse response = new OrderPageListResponse();
            BeanUtils.copyProperties(record, response);

            String statusDesc;
            int statusCode;
            //如果支付成功,返回的状态为已完成
            switch (OrderStatusEnum.getEnumByCode(record.getOrderState())) {
                case NO_START:
                case CHARGING:
                case GUN_NOT_DRAWN:
                    statusCode = MiniStatusEnum.UNDERWAY.getCode();
                    statusDesc = MiniStatusEnum.UNDERWAY.getDesc();
                    break;
                case CHARGING_COMPLETED:
                    statusCode = MiniStatusEnum.SETTLING.getCode();
                    statusDesc = MiniStatusEnum.SETTLING.getDesc();
                    break;
                case BILL_HAS_BEEN_UPLOADED:
                    if (record.getPayStatus().equals(OrderPayStatusEnum.PAY_SUCCESS.getCode())) {
                        statusCode = MiniStatusEnum.COMPLETE.getCode();
                        statusDesc = MiniStatusEnum.COMPLETE.getDesc();
                    } else {
                        statusCode = MiniStatusEnum.PENDING_PAY.getCode();
                        statusDesc = MiniStatusEnum.PENDING_PAY.getDesc();
                    }
                    break;
                default:
                    statusCode = MiniStatusEnum.ABNORMAL.getCode();
                    statusDesc = MiniStatusEnum.ABNORMAL.getDesc();
                    break;
            }

            response.setStatus(statusCode);
            response.setStatusDesc(statusDesc);
            if (ObjectUtils.isEmpty(record.getEndTime())) {
                record.setEndTime(new Date());
            }
            if (ObjectUtils.isNotEmpty(record.getStartTime())) {
                response.setRealDuration(DateUtils.getDatePoor(record.getEndTime(), record.getStartTime()));
            }
            response.setRealAmount(record.getRealAmount());
            // 判断是否已评价
            boolean exists = stationReviewsMapper.exists(Wrappers.<ChargingStationReviews>lambdaQuery()
                    .eq(ChargingStationReviews::getOrderId, record.getId())
                    .eq(ChargingStationReviews::getAppUserId, SecurityUtils.getUserId())
            );
            response.setReviewed(exists);
            responseList.add(response);
        }
        responsePage.setTotal(orderPage.getTotal());
        responsePage.setRecords(responseList);

        return responsePage;
    }

    /**
     * 小程序-充电订单详情
     */
    @Override
    public OrderDetailResponse miniOrderDetail(String tradeNo) {
        OrderDetailResponse response = new OrderDetailResponse();
        ChargingOrder order = baseMapper.selectOne(Wrappers.<ChargingOrder>lambdaQuery().eq(ChargingOrder::getTradeNo, tradeNo));
        if (ObjectUtils.isEmpty(order)) {
            throw new ServiceException(MessageConstants.ORDER_NOT_EXIST);
        }
        BeanUtils.copyProperties(order, response);

        MiniStatusEnum complete;
        //如果支付成功,返回的状态为已完成
        if (order.getPayStatus().equals(OrderPayStatusEnum.PAY_SUCCESS.getCode())) {
            response.setStatus(MiniStatusEnum.COMPLETE.getCode());
            complete = MiniStatusEnum.COMPLETE;
        } else {
            //如果支付状态为未支付,返回的状态为待支付
            complete = MiniStatusEnum.PENDING_PAY;
        }
        //如果订单状态不是充电结束,返回的状态为进行中
        if (order.getOrderState().equals(OrderStatusEnum.CHARGING.getCode())
                || order.getOrderState().equals(OrderStatusEnum.NO_START.getCode())
                || order.getOrderState().equals(OrderStatusEnum.GUN_NOT_DRAWN.getCode())) {
            complete = MiniStatusEnum.UNDERWAY;
        } else if (order.getOrderState().equals(OrderStatusEnum.CHARGING_COMPLETED.getCode())) {
            complete = MiniStatusEnum.SETTLING;
        }
        response.setStatus(complete.getCode());
        response.setStatusDesc(complete.getDesc());

        response.setTradeNo(order.getTradeNo());
        response.setOrderSourceDesc(OrderSourceEnum.getDescByCode(order.getOrderSource()));
        response.setPaymentDesc(OrderPayTypeEnum.getDescByCode(order.getPayment()));
        if (ObjectUtils.isEmpty(order.getEndTime())) {
            order.setEndTime(new Date());
        }
        if (ObjectUtils.isNotEmpty(order.getStartTime())) {
            response.setRealDuration(DateUtils.getDatePoor(order.getEndTime(), order.getStartTime()));
        }
        return response;
    }

    /**
     * 查询用户待支付订单
     */
    @Override
    public PendingPaymentOrderResponse getPendingPaymentOrder() {
        PendingPaymentOrderResponse response = new PendingPaymentOrderResponse();
        String userOpenId = SecurityUtils.getUserOpenId();
        if (ObjectUtils.isEmpty(userOpenId)) {
            throw new ServiceException(MessageConstants.NOT_OBTAINED_LOGIN_INFO);
        }
        ChargingOrder chargingOrder = baseMapper.selectOne(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getOpenId, userOpenId)
                .in(ChargingOrder::getOrderState,
                        OrderStatusEnum.NO_START.getCode(),
                        OrderStatusEnum.CHARGING.getCode(),
                        OrderStatusEnum.CHARGING_COMPLETED.getCode()
                )
                .last("limit 1"));
        if (ObjectUtils.isNotEmpty(chargingOrder)) {
            BeanUtils.copyProperties(chargingOrder, response);
            return response;
        }

        // 没有充电中订单就查询待支付订单
        ChargingOrder pendingPaymentOrder = baseMapper.selectOne(Wrappers.<ChargingOrder>lambdaQuery()
                .eq(ChargingOrder::getOpenId, userOpenId)
                .eq(ChargingOrder::getOrderState, OrderStatusEnum.BILL_HAS_BEEN_UPLOADED.getCode())
                .eq(ChargingOrder::getPayStatus, OrderPayStatusEnum.NOT_PAY.getCode())
                .last("limit 1"));
        if (ObjectUtils.isNotEmpty(pendingPaymentOrder)) {
            BeanUtils.copyProperties(pendingPaymentOrder, response);
        }
        return response;
    }

    /**
     * 根据订单号查询订单状态
     *
     * @param tradeNo 订单id
     * @return 订单详情
     */
    @Override
    public MiniOrderDetailResponse getOrderInfoByTradeNo(String tradeNo) {
        MiniOrderDetailResponse response = new MiniOrderDetailResponse();
        ChargingOrder chargingOrder = baseMapper.selectOne(Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getOrderState,
                        ChargingOrder::getDeviceNo,
                        ChargingOrder::getGunNo,
                        ChargingOrder::getStationName,
                        ChargingOrder::getStationId,
                        ChargingOrder::getEquipmentSourceOperator)
                .eq(ChargingOrder::getTradeNo, tradeNo));
        if (ObjectUtils.isNotEmpty(chargingOrder)) {
            String gunNo = chargingOrder.getGunNo();
            String deviceNo = chargingOrder.getDeviceNo();
            String stationName = chargingOrder.getStationName();

            response.setGunsNo(gunNo);
            response.setTradeNo(tradeNo);
            response.setDeviceNo(deviceNo);
            response.setStationName(stationName);
            response.setStationId(chargingOrder.getStationId());
            response.setOperatorId(chargingOrder.getEquipmentSourceOperator());
            response.setOrderState(OrderStatusEnum.getEnumByCode(chargingOrder.getOrderState()).name());
        }

        return response;
    }

    /**
     * 查询是否存在未支付订单
     *
     * @return 结果
     */
    @Override
    public boolean existsUnpaidOrders(String openId) {
        return baseMapper.exists(Wrappers.<ChargingOrder>lambdaQuery()
                .in(ChargingOrder::getOrderState, OrderStatusEnum.CHARGING.getCode(), OrderStatusEnum.GUN_NOT_DRAWN.getCode(),
                        OrderStatusEnum.CHARGING_COMPLETED.getCode(), OrderStatusEnum.BILL_HAS_BEEN_UPLOADED.getCode())
                .eq(ChargingOrder::getPayStatus, OrderPayStatusEnum.NOT_PAY.getCode())
                .eq(ChargingOrder::getOpenId, openId)
        );
    }

    /**
     * 构造订单查询条件
     */
    private LambdaQueryWrapper<ChargingOrder> getOrderQueryWrapper(ChargingOrderPageListRequest request) {
        LambdaQueryWrapper<ChargingOrder> queryWrapper = Wrappers.<ChargingOrder>lambdaQuery()
                .select(ChargingOrder::getId, ChargingOrder::getTradeNo, ChargingOrder::getOutTradeNo, ChargingOrder::getOrderState,
                        ChargingOrder::getStationName, ChargingOrder::getPayTime, ChargingOrder::getPayStatus, ChargingOrder::getCouponAmount,
                        ChargingOrder::getDeviceNo, ChargingOrder::getGunNo, ChargingOrder::getMobile, ChargingOrder::getPlateNo,
                        ChargingOrder::getTotalAmount, ChargingOrder::getChargeFee, ChargingOrder::getServiceFee,
                        ChargingOrder::getParkingFee, ChargingOrder::getOverTimeFee, ChargingOrder::getTotalPower,
                        ChargingOrder::getStartTime, ChargingOrder::getEndTime)
                .eq(ObjectUtils.isNotEmpty(request.getStationId()), ChargingOrder::getStationId, request.getStationId())
                .like(ObjectUtils.isNotEmpty(request.getTradeNo()), ChargingOrder::getTradeNo, request.getTradeNo())
                .like(ObjectUtils.isNotEmpty(request.getDeviceNo()), ChargingOrder::getDeviceNo, request.getDeviceNo())
                .like(ObjectUtils.isNotEmpty(request.getGunNo()), ChargingOrder::getGunNo, request.getGunNo())
                .like(ObjectUtils.isNotEmpty(request.getMobile()), ChargingOrder::getMobile, request.getMobile())
                .like(ObjectUtils.isNotEmpty(request.getPlateNo()), ChargingOrder::getPlateNo, request.getPlateNo())
                .eq(ObjectUtils.isNotEmpty(request.getOrderState()), ChargingOrder::getOrderState, request.getOrderState())
                .eq(ObjectUtils.isNotEmpty(request.getOrderSource()), ChargingOrder::getOrderSource, request.getOrderSource())
                .eq(ObjectUtils.isNotEmpty(request.getOffline()), ChargingOrder::getOffline, request.getOffline())
                .eq(ObjectUtils.isNotEmpty(request.getPlatform()), ChargingOrder::getOperatorId, request.getPlatform())
                .eq(ObjectUtils.isNotEmpty(request.getDevicePlatform()), ChargingOrder::getEquipmentSourceOperator, request.getDevicePlatform())
                .ge(ObjectUtils.isNotEmpty(request.getStartTime()), ChargingOrder::getCreateTime, request.getStartTime())
                .le(ObjectUtils.isNotEmpty(request.getEndTime()), ChargingOrder::getCreateTime, request.getEndTime())
                .like(ObjectUtils.isNotEmpty(request.getOutTradeNo()), ChargingOrder::getOutTradeNo, request.getOutTradeNo())
                .orderByDesc(ChargingOrder::getCreateTime);

        OrderPayStatusEnum payStatus = request.getPayStatus();
        if (payStatus != null) {
            queryWrapper.eq(ChargingOrder::getPayStatus, payStatus.getCode());
        }
        // 根据用户类型查询用户openId
        if (!request.getUserType().isEmpty()) {
            // 获取用户openId列表
            List<String> openIdList = getOpenIdList(request);
            if (!openIdList.isEmpty()) {
                queryWrapper.in(ChargingOrder::getOpenId, openIdList);
            }
        }

        return queryWrapper;
    }

    /**
     * 获取用户openId列表
     */
    private List<String> getOpenIdList(ChargingOrderPageListRequest request) {
        return appUserMapper.selectList(
                        Wrappers.<AppUser>lambdaQuery()
                                .select(AppUser::getOpenId)
                                .in(AppUser::getType, request.getUserType()))
                .stream().map(AppUser::getOpenId).collect(Collectors.toList());
    }

    /**
     * 获取订单统计数据
     *
     * @param orderList 订单信息
     * @param response  结果
     */
    private void getOrderStatisticsData(List<ChargingOrder> orderList, OrderStatisticsResponse response) {
        if (orderList.isEmpty()) {
            return;
        }
        //订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        //电费
        BigDecimal electricityFee = BigDecimal.ZERO;
        //服务费
        BigDecimal serviceFee = BigDecimal.ZERO;
        //停车费
        BigDecimal parkingFee = BigDecimal.ZERO;
        //超时占用费
        BigDecimal occupancyFee = BigDecimal.ZERO;
        //充电量
        BigDecimal chargingCapacity = BigDecimal.ZERO;
        //优惠总金额
        BigDecimal discountAmount = BigDecimal.ZERO;
        for (ChargingOrder order : orderList) {
            totalAmount = totalAmount.add(order.getTotalAmount());
            electricityFee = electricityFee.add(order.getChargeFee());
            serviceFee = serviceFee.add(order.getServiceFee());
            parkingFee = parkingFee.add(order.getParkingFee());
            occupancyFee = occupancyFee.add(order.getOverTimeFee());
            chargingCapacity = chargingCapacity.add(order.getTotalPower());
            discountAmount = discountAmount.add(order.getCouponAmount());
        }
        long chargingDuration = orderList.stream().filter(ObjectUtils::isNotEmpty).mapToLong(order -> {
                    if (ObjectUtils.isNotEmpty(order.getStartTime()) && ObjectUtils.isNotEmpty(order.getEndTime())) {
                        if (order.getEndTime().after(order.getStartTime())) {
                            return Duration.between(order.getStartTime().toInstant(), order.getEndTime().toInstant()).getSeconds();
                        } else {
                            return 0L;
                        }
                    }
                    return 0L;
                }
        ).sum();
        if (chargingDuration > 0) {
            response.setChargingDuration(DateUtils.formatDateTime(chargingDuration));
        }
        response.setOrderCount(orderList.size());
        response.setOrderTotalAmount(totalAmount);
        response.setElectricityFee(electricityFee);
        response.setServiceFee(serviceFee);
        response.setParkingFee(parkingFee);
        response.setOccupancyFee(occupancyFee);
        response.setChargingCapacity(chargingCapacity);
        response.setDiscountAmount(discountAmount);
    }

    /**
     * 获取实时数据
     */
    private void getOrderRealTimeInfo(ChargingOrder record, ChargingOrderPageListResponse response) {
        String key = String.format(CacheConstants.DEVICE_STATUS_KEY, record.getDeviceNo(), record.getGunNo());
        String jsonStr = redisCache.getCacheObject(key);
        if (ObjectUtils.isNotEmpty(jsonStr)) {
            ChargingRealtimeDataModel realtimeData = JSON.parseObject(jsonStr, ChargingRealtimeDataModel.class);
            response.setTotalPower(BigDecimal.valueOf(realtimeData.getTotalPower()));
            response.setTotalAmount(BigDecimal.valueOf(realtimeData.getTotalAmount()));
        }
    }

}
