package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    NONE(-1, "未知"),
    NO_START(0, "未开始"),
    CHARGING(1, "充电中"),
    GUN_NOT_DRAWN(2, "充电结束（未拔枪）"),
    CHARGING_COMPLETED(3, "充电结束（待结算）"),
    BILL_HAS_BEEN_UPLOADED(4, "账单已上传"),
    ABNORMAL(5, "异常");

    private final Integer code;
    private final String desc;

    public static OrderStatusEnum getEnumByCode(Integer code) {
        for (OrderStatusEnum e : OrderStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return OrderStatusEnum.NONE;
    }

    public static String getDescByCode(Integer code) {
        for (OrderStatusEnum e : OrderStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return StringUtils.EMPTY;
    }


}