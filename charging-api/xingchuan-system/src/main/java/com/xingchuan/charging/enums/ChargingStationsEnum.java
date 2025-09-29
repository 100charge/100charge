package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站枚举
 */
@Getter
@AllArgsConstructor
public enum ChargingStationsEnum {

    /**
     * 关闭
     */
    CLOSED(0, "关闭"),
    /**
     * 开放
     */
    OPEN(1, "开放"),
    /**
     * 维护中
     */
    MAINTAIN(2, "维护中");


    @EnumValue
    private final int code;
    private final String name;

    public static String getName(int code) {
        for (ChargingStationsEnum appUserEnum : ChargingStationsEnum.values()) {
            if (appUserEnum.getCode() == code) {
                return appUserEnum.getName();
            }
        }
        return "";
    }

}
