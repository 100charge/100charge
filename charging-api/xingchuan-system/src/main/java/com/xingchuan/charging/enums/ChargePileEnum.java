package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 充电桩枚举
 */
@Getter
@AllArgsConstructor
public enum ChargePileEnum {

    AC(0, "交流"),
    DC(1, "直流");

    private final Integer code;
    private final String desc;

    public static String getDescByCode(Integer code) {
        for (ChargePileEnum e : ChargePileEnum.values()) {
            if (e.code.equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    public static Integer getCodeByDesc(String code) {
        for (ChargePileEnum e : ChargePileEnum.values()) {
            if (e.desc.equals(code)) {
                return e.code;
            }
        }
        return null;
    }


}