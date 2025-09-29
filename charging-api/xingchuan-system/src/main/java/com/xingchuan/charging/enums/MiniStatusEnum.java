package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 小程序订单列表查询枚举
@Getter
@AllArgsConstructor
public enum MiniStatusEnum {
    ABNORMAL(-1, "异常"),
    UNDERWAY(0, "进行中"),
    PENDING_PAY(1, "待支付"),
    COMPLETE(2, "已完成"),
    SETTLING(3, "结算中"),
    ;
    private final Integer code;
    private final String desc;

    public static MiniStatusEnum getEnumByCode(Integer code) {
        for (MiniStatusEnum e : MiniStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (MiniStatusEnum e : MiniStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}