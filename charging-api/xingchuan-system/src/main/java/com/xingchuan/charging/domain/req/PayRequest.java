package com.xingchuan.charging.domain.req;

import com.xingchuan.charging.enums.PaySource;
import com.xingchuan.charging.enums.Payment;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付请求
 */
@Data
public class PayRequest {
    /**
     * 金额（元）
     */
    private BigDecimal amount;
    /**
     * 方式
     */
    private Payment payment;
    /**
     * 支付来源
     */
    private PaySource paySource;
}
