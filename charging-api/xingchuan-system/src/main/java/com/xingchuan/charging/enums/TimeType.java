package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间类型枚举
 */
@Getter
@AllArgsConstructor
public enum TimeType {

    // 小时
    HOUR,
    // 天
    DAY,
    // 星期
    WEEK,
    // 月
    MONTH,
    // 年
    YEAR
}