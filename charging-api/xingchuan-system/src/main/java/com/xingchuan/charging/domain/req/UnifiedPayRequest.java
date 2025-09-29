package com.xingchuan.charging.domain.req;

import com.xingchuan.charging.enums.PaySource;
import com.xingchuan.charging.enums.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 基础的支付请求实体类
 */
@Data
public class UnifiedPayRequest {
    /**
     * 支付方式
     */
    private Payment payment;
    /**
     * 支付相关的来源 H5 APP 小程序等
     */
    private PaySource paySource;
    /**
     * 订单总金额，单位为元，精确到小数点后两位，
     * 取值范围[0.01,100000000]，金额不能为0
     */
    private BigDecimal amount;
    /**
     * 手续费(部分接口使用)
     */
    private BigDecimal handlingFee;
    /**
     * 商户订单的唯一id
     */
    private String outTradeNo;
    /**
     * 微信的openid或者支付宝的buyerOpenId
     */
    private String openId;
    /**
     * 【商品描述】商品信息描述，用户微信账单的商品字段中可见(可参考小程序支付示例说明-账单示意图)，
     * 服务商需传递能真实代表商品信息的描述，不能超过127个字符。
     */
    private String description;
    /**
     * 额外参数
     * 1. 通联的PayMethod
     */
    private Map<String, Object> extraParams;
    /**
     * 附带参数
     */
    private String extraInfo;
}
