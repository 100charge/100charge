package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易状态
 */
@Getter
@AllArgsConstructor
public enum PayState {
    ERROR(-1, "错误"),
    SUCCESS(0, "支付成功"),
    REFUND(1, "转入退款"),
    NOT_PAY(2, "未支付"),
    CLOSED(3, "已关闭");

    private final int state;
    private final String desc;
}
