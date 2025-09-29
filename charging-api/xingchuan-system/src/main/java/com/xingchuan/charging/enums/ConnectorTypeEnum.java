package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 充电设备接口类型
 */
@Getter
@AllArgsConstructor
public enum ConnectorTypeEnum {
    HOUSEHOLD_OUTLETS(1, "HOUSEHOLD_OUTLETS", "家用插座（模式2）"),
    AC_DOCK(2, "AC_DOCK", "交流接口插座（模式3，连接方式B ）"),
    AC_PLUG(3, "AC_PLUG", "交流接口插头（带枪线，模式3，连接方式C）"),
    DC_PLUG(4, "DC_PLUG", "直流接口枪头（带枪线，模式4）"),
    WIRELESS_CHARGING_DOCK(5, "WIRELESS_CHARGING_DOCK", "无线充电座"),
    OTHER(6, "OTHER", "其他"),
    ;
    private final int code;
    private final String name;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static ConnectorTypeEnum getConnectorType(int code) {
        for (ConnectorTypeEnum value : ConnectorTypeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return OTHER;
    }


    /**
     * 根据code获取枚举
     */
    public static String getConnectorTypeDesc(int code) {
        for (ConnectorTypeEnum value : ConnectorTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.desc;
            }
        }
        return OTHER.desc;
    }
}
