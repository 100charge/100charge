package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 订单支付方式枚举
@Getter
@AllArgsConstructor
public enum OrderPayTypeEnum {
    BALANCE(0, "余额"),
    CARD_PAY(1, "卡支付"),
    PAY_POINTS(2, "微信支付分"),
    CREDIT_POINTS(3, "支付宝信用分"),
    PREPAID_FEES(4, "预付费"),
    HLHT(5, "互联互通挂账"),
    ENTERPRISE_BALANCE(6, "企业余额"),
    ;

    private final Integer code;
    private final String name;

    public static OrderPayTypeEnum getEnumByCode(Integer code) {
        for (OrderPayTypeEnum e : OrderPayTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (OrderPayTypeEnum e : OrderPayTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.name;
            }
        }
        return null;
    }
}