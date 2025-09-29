package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 充电桩状态枚举
 */
@Getter
@AllArgsConstructor
public enum ChargeGunsEnum {

    OFFLINE(0, "OFFLINE", "离线"),
    FAULT(1, "FAULT", "故障"),
    ONLINE(2, "ONLINE", "空闲"),
    STARTING(3, "STARTING", "启动中"),
    CHARGING(4, "CHARGING", "充电中"),
    GUN_INSERTED(5, "GUN_INSERTED", "已插枪"),
    FINISHED(6, "FINISHED", "已结束");

    private final int code;
    private final String name;
    private final String desc;

}
