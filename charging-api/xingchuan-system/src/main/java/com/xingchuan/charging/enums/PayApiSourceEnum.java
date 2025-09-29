package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付api的来源
 */
@Getter
@AllArgsConstructor
public enum PayApiSourceEnum {

    UNKNOWN(-1, "UNKNOWN", "未知"),
    ALL_IN(0, "ALL_IN", "通联"),
    ALI_PAY(1, "ALI_PAY", "支付宝"),
    WECHAT_PAY(2, "WECHAT_PAY", "微信支付"),
    ;

    private final Integer code;
    private final String name;
    private final String desc;
}
