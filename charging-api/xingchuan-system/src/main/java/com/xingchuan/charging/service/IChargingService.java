package com.xingchuan.charging.service;


import com.xingchuan.charging.domain.req.OrderSettlementRequest;
import com.xingchuan.charging.domain.req.StartChargingRequest;
import com.xingchuan.charging.domain.resp.ChargingRealtimeDataStatistics;

public interface IChargingService {

    /**
     * 获取桩的实时状态
     */
    ChargingRealtimeDataStatistics getRealTimeStatus(Long stationId);

    /**
     * 开始充电
     *
     * @param request 请求参数
     */
    String startCharging(StartChargingRequest request);

    /**
     * 结束充电
     *
     * @param tradeNo 订单编号
     */
    String endCharge(String tradeNo);

    /**
     * 订单支付
     *
     * @param request 请求参数
     */
    void orderPayment(OrderSettlementRequest request);


}
