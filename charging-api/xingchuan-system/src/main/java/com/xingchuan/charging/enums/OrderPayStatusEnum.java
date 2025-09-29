package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 订单支付状态
@Getter
@AllArgsConstructor
public enum OrderPayStatusEnum {
    NOT_PAY(0, "未支付"),
    PAY_SUCCESS(1, "支付成功"),
    PAY_FAIL(2, "支付失败"),
    PAYMENT_SETTLEMENT(3, "支付结算"),
    ;

    private final Integer code;
    private final String desc;

    public static OrderPayStatusEnum getEnumByCode(Integer code) {
        for (OrderPayStatusEnum e : OrderPayStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (OrderPayStatusEnum e : OrderPayStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}