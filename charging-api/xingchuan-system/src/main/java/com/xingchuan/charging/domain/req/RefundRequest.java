package com.xingchuan.charging.domain.req;

import com.xingchuan.charging.enums.PaySource;
import com.xingchuan.charging.enums.Payment;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款请求
 */
@Data
public class RefundRequest {
    /**
     * 金额（元）
     */
    private BigDecimal amount;
    /**
     * 退款方式
     */
    private Payment payment;
    /**
     * 支付来源
     */
    private PaySource paySource;
}
