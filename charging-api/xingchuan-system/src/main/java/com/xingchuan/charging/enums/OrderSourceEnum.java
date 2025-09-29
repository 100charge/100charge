package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 订单来源
@Getter
@AllArgsConstructor
public enum OrderSourceEnum {
    WECHAT(1, "微信小程序"),
    ALIPAY(2, "支付宝小程序"),
    KEY_CARD(3, "钥匙卡"),
    VIN(4, "VIN"),
    HLHT(5, "互联互通"),
    RESALE(6, "三方互联");

    private final Integer code;
    private final String desc;

    public static OrderSourceEnum getEnumByCode(Integer code) {
        for (OrderSourceEnum e : OrderSourceEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (OrderSourceEnum e : OrderSourceEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}