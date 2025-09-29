package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 支付相关的来源
 */
@Getter
@AllArgsConstructor
public enum PaySource {
    UNKNOWN("UNKNOWN", "UNKNOWN"),
    WECHAT("WECHAT", "微信"),
    ALIPAY("ALIPAY", "支付宝"),
    APP("APP", "APP"),
    H5("H5", "H5网页"),
    NATIVE("NATIVE", "NATIVE"),
    MINI_PRO("MINI_PRO", "MINI_PRO"),
    ;
    private final String code;
    private final String desc;

    public static PaySource getByCode(String code) {
        for (PaySource value : PaySource.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
