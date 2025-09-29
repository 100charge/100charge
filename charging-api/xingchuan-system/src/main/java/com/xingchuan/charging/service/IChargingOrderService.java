package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.ChargingOrder;
import com.xingchuan.charging.domain.excel.ChargingOrderExport;
import com.xingchuan.charging.domain.req.ChargingOrderPageListRequest;
import com.xingchuan.charging.domain.resp.*;

import java.util.List;


/**
 * 充电订单Service接口
 *
 * @author ruoyi
 */
public interface IChargingOrderService extends IService<ChargingOrder> {

    /**
     * 充电订单列表分页查询
     */
    Page<ChargingOrderPageListResponse> selectPageList(ChargingOrderPageListRequest request);

    /**
     * 充电订单统计
     */
    OrderStatisticsResponse orderStatistics(ChargingOrderPageListRequest request);

    /**
     * 充电订单详情查询
     */
    ChargingOrderDetailResponse orderDetail(Long id);

    /**
     * 充电订单列表导出
     */
    List<ChargingOrderExport> export(ChargingOrderPageListRequest request);

    /**
     * 未支付的订单进行主动扣款
     */
    void autoPayOrder();

    /**
     * 置为异常订单
     *
     * @param tradeNo 订单号
     */
    void markOrderAsAbnormal(String tradeNo);

    /**
     * 小程序充电订单列表查询
     */
    Page<OrderPageListResponse> miniOrderList(Integer status);

    /**
     * 小程序充电订单详情查询
     */
    OrderDetailResponse miniOrderDetail(String tradeNo);

    /**
     * 查询用户待支付订单
     */
    PendingPaymentOrderResponse getPendingPaymentOrder();

    /**
     * 根据订单号查询订单详情
     *
     * @param tradeNo 订单编号
     * @return 结果
     */
    MiniOrderDetailResponse getOrderInfoByTradeNo(String tradeNo);

    /**
     * 查询是否存在未支付订单
     *
     * @param openId 用户的openid
     * @return 结果
     */
    boolean existsUnpaidOrders(String openId);


}
