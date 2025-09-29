package com.xingchuan.charging.domain.resp;

import lombok.Data;

@Data
public class BaseRefundResponse {
    /**
     * 商户退款订单号
     */
    private String tradeNo;
    /**
     * 三方退款订单号
     */
    private String outRefundNo;
    /**
     * 退款状态
     */
    private RefundState state;
}
