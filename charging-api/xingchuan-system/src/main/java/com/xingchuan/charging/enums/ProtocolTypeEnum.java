package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议类型枚举
 */
@Getter
@AllArgsConstructor
public enum ProtocolTypeEnum {

    UNKNOWN(-1, "未知协议未知"),
    YUN_15(0, "云快充协议1.5"),
    YUN_16(1, "云快充协议1.6"),
    TELD_AC(2, "特来电交流协议2021新版"),
    TELD_DC(3, "特来电直流协议2021-2024"),
    HLHT(4, "互联互通"),
    ;

    private final int code;
    private final String name;

    /**
     * 根据code获取枚举
     */
    public static ProtocolTypeEnum getEnumByCode(int code) {
        for (ProtocolTypeEnum value : ProtocolTypeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static String getDescByCode(int code) {
        for (ProtocolTypeEnum value : ProtocolTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return UNKNOWN.name;
    }

    public static int getCodeByEnumName(String enumName) {
        for (ProtocolTypeEnum value : ProtocolTypeEnum.values()) {
            if (value.name().equals(enumName)) {
                return value.getCode();
            }
        }
        return UNKNOWN.getCode();
    }
}
