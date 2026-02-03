package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 余额订单状态
 */
@Getter
@AllArgsConstructor
public enum BalanceRecordStatusEnum {

    INVALID(-1, "失效"),
    PROCESSING(0, "处理中"),
    SUCCESS(1, "已完成"),
    FAILURE(2, "失败");

    @EnumValue
    private final int code;
    private final String desc;

    public static String getStatusDesc(int status) {
        switch (status) {
            case -1:
                return "失效";
            case 0:
                return "处理中";
            case 1:
                return "已完成";
            case 2:
            default:
                return "";
        }
    }
}