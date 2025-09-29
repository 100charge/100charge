package com.xingchuan.charging.domain.req;

import com.xingchuan.charging.enums.PaySource;
import com.xingchuan.charging.enums.Payment;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款请求基类
 */
@Data
public class UnifiedRefundRequest {
    /**
     * 支付方式
     */
    private Payment payment;
    /**
     * 来源
     */
    private PaySource source;
    /**
     * 退款金额
     */
    private BigDecimal amount;
    /**
     * 微信退款必填
     * 【原订单金额】 原支付交易的订单总金额，币种的最小单位，只能为整数
     */
    private BigDecimal payOrderAmount;
    /**
     * 商户自己的退款单号
     */
    private String refundNo;
    /**
     * 退款原因说明
     */
    private String refundReason;
    /**
     * 三方支付（充值）订单号，每次支付都会返回
     */
    private String tradeNo;
    /**
     * 商户支付（充值）订单号
     */
    private String bizTradeNo;
    /**
     * 支付宝使用
     * 【描述】退款请求号。 标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传。
     * 注：针对同一次退款请求，如果调用接口失败或异常了，重试时需要保证退款请求号不能变更，
     * 防止该笔交易重复退款。支付宝会保证同样的退款请求号多次请求只会退一次。
     * 【必选条件】部分退款时必选
     */
    private String outRequestNo;
    /**
     * 退款人id
     */
    private String openId;
}
