package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 支付渠道枚举
 */
@Getter
@AllArgsConstructor
public enum Payment {
    UNKNOWN("UNKNOWN", "未知"),
    WECHAT_PAY("WECHAT_PAY", "微信支付"),
    ALI_PAY("ALI_PAY", "支付宝支付"),
    ALLIN_PAY("ALLIN_PAY", "通联支付"),
    UNION_PAY("UNION_PAY", "银联支付"),
    YEE_PAY("YEE_PAY", "易宝支付"),
    ;
    private final String code;
    private final String desc;

    public static Payment getByCode(String code) {
        for (Payment value : Payment.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
