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
}