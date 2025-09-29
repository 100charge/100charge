package com.xingchuan.charging.wechat.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信支付请求枚举
 */
@Getter
@AllArgsConstructor
public enum WechatRequestMethod {

    JSAPI_PREPAY("/v3/pay/partner/transactions/jsapi", "JSAPI/小程序下单"),
    PAY_NOTIFY("/pay/payNotify", "支付成功回调通知"),
    CLOSE_ORDER("/v3/pay/partner/transactions/out-trade-no/{out_trade_no}/close", "关闭订单"),
    QUERY_BY_TRANSACTION_ID("/v3/pay/partner/transactions/id/{transaction_id}", "微信支付订单号查询订单"),
    QUERY_BY_OUT_TRADE_NO("/v3/pay/partner/transactions/out-trade-no/{out_trade_no}", "商户订单号查询订单"),
    REFUNDS("/v3/refund/domestic/refunds", "申请退款"),
    QUERY_REFUNDS("/v3/refund/domestic/refunds/{out_refund_no}", "查询单笔退款（通过商户退款单号）"),
    APPLY_ABNORMAL_REFUND("/v3/refund/domestic/refunds/{refund_id}/apply-abnormal-refund", "发起异常退款"),
    REFUNDS_NOTIFY("/pay/refundNotify", "退款结果回调通知"),
    ;
    private final String method;
    private final String description;
}
