package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 尖峰平谷费率
 * Time-of-Use Rates
 */
@Getter
@AllArgsConstructor
public enum TOURatesEnum {
    SUPER_PEAK((byte) 0x00, "SuperPeak", "尖峰"),
    PEAK((byte) 0x01, "Peak", "高峰"),
    OFF_PEAK((byte) 0x02, "OffPeak", "平段"),
    VALLEY((byte) 0x03, "Valley", "谷段"),
    DEEP((byte) 0x04, "Deep", "深谷"),
    ;
    private final byte code;
    private final String name;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static TOURatesEnum getByCode(byte code) {
        for (TOURatesEnum value : TOURatesEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return SUPER_PEAK;
    }
}
