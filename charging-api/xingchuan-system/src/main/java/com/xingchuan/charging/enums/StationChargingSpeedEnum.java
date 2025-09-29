package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站-充电速度枚举
 */
@Getter
@AllArgsConstructor
public enum StationChargingSpeedEnum {

    /**
     * 超充
     */
    SUPERCHARGE,
    /**
     * 快充
     */
    FAST_CHARGE,
    /**
     * 慢充
     */
    SLOW_CHARGE
}