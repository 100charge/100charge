package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 充电桩状态枚举
@Getter
@AllArgsConstructor
public enum PileStatusEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private final Integer code;
    private final String desc;

    public static PileStatusEnum getEnumByCode(Integer code) {
        for (PileStatusEnum e : PileStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (PileStatusEnum e : PileStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}