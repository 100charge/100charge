package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleTypeEnum {
    TIP(0, "尖"),
    PEAK(1, "峰"),
    FLAT(2, "平"),
    VALLEY(3, "谷"),
    DEEP_VALLEY(4, "深谷");

    private final Integer code;
    private final String desc;

    public static RuleTypeEnum getEnumByCode(Integer code) {
        for (RuleTypeEnum e : RuleTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (RuleTypeEnum e : RuleTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
} 