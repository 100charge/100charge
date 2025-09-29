package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单来源
 */
@Getter
@AllArgsConstructor
public enum TradeNoSourceEnum {

    UNKNOWN("0000", "未知"),
    WE_CHAT("0001", "微信"),
    ALI_PAT("0002", "支付宝"),
    CARD("0003", "刷卡"),
    VIN("0004", "VIN"),
    HLHT("0005", "互联互通"),
    ;

    private final String code;
    private final String name;

    /**
     * 根据code获取枚举
     */
    public static TradeNoSourceEnum getByCode(String code) {
        for (TradeNoSourceEnum value : TradeNoSourceEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
