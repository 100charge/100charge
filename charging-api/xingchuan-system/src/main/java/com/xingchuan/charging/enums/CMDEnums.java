package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备命令枚举
 */
@Getter
@AllArgsConstructor
public enum CMDEnums {

    UNKNOWN(0, "UNKNOWN", "未知"),
    START(1, "START", "启动充电"),
    STOP(2, "STOP", "停止充电"),
    REBOOT(3, "REBOOT", "重启设备"),
    ADJUSTMENT(4, "ADJUSTMENT", "功率调节"),
    BILL_PUSH(8, "BILL_PUSH", "结算单推送"),
    SEND_QR_CODE(9, "SEND_QR_CODE", "下发二维码"),
    ;


    private final int code;
    private final String name;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static CMDEnums getByCode(byte code) {
        for (CMDEnums value : CMDEnums.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
