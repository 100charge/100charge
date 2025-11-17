package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppUserCarEnum {

    PRIVATE_CAR(0, "私家车"),

    RIDE_HAILING_CAR(1, "网约车"),

    TAXI(2, "出租车"),

    LOGISTICS_VEHICLE(3, "物流车");

    @EnumValue
    private final Integer code;
    private final String desc;

    public static String getDesc(Integer code) {
        for (AppUserCarEnum appUserCarEnum : AppUserCarEnum.values()) {
            if (appUserCarEnum.getCode().equals(code)) {
                return appUserCarEnum.getDesc();
            }
        }
        return null;
    }
}