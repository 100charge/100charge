package com.xingchuan.charging.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 退款状态
 */
@Getter
@AllArgsConstructor
public enum RefundState {
    SUCCESS("SUCCESS", "退款成功"),
    CLOSED("CLOSED", "退款关闭"),
    PROCESSING("PROCESSING", "退款处理中"),
    ABNORMAL("ABNORMAL", "退款异常");

    private final String code;
    private final String desc;

    public static RefundState getByCode(String code) {
        for (RefundState value : RefundState.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return PROCESSING;
    }
}
