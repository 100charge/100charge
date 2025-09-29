package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款明细状态
 */
@Getter
@AllArgsConstructor
public enum RefundDetailStatusEnum {
    PROCESSING(0, "处理中"),
    REFUND_SUCCESS(1, "退款成功"),
    REFUND_FAIL(2, "退款失败");

    private final Integer code;
    private final String desc;

    public static RefundDetailStatusEnum getEnumByCode(Integer code) {
        for (RefundDetailStatusEnum e : RefundDetailStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (RefundDetailStatusEnum e : RefundDetailStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}