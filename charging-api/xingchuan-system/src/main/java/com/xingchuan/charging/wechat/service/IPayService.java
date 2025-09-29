package com.xingchuan.charging.wechat.service;


import com.xingchuan.charging.domain.req.PayRequest;
import com.xingchuan.charging.domain.req.RefundRequest;
import com.xingchuan.charging.wechat.model.pay.WechatPayResponse;

/**
 * 支付接口
 */
public interface IPayService {

    /**
     * 充值
     *
     * @param payRequest 支付请求
     * @return 支付返回参数
     */
    WechatPayResponse recharge(PayRequest payRequest);

    /**
     * 退款
     *
     * @param request 退款请求
     */
    void refund(RefundRequest request);
}
