package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站-列表检索枚举
 */
@Getter
@AllArgsConstructor
public enum StationSearchEnum {

    /**
     * 智能推荐
     */
    AI,
    /**
     * 距离优先
     */
    DISTANCE,
    /**
     * 价格优先
     */
    PRICE
}