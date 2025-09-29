package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 订单优惠状态
@Getter
@AllArgsConstructor
public enum OrderDiscountTypeEnum {

    ENUM0(0, "满减券-服务费"),
    ENUM1(1, "满减券-电费+服务费"),
    ENUM2(2, "直减券-服务费"),
    ENUM3(3, "直减券-电费+服务费"),
    ENUM4(4, "折扣券-服务费%"),
    ENUM5(5, "场站优惠");

    private final Integer code;
    private final String desc;

    public static OrderDiscountTypeEnum getEnumByCode(Integer code) {
        for (OrderDiscountTypeEnum e : OrderDiscountTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (OrderDiscountTypeEnum e : OrderDiscountTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}