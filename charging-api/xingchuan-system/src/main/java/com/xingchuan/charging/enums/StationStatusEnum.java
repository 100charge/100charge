package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站-场站状态
 */
@Getter
@AllArgsConstructor
public enum StationStatusEnum {

    /**
     * 关闭
     */
    SHUT_DOWN(0),
    /**
     * 开放
     */
    OPENING(1),
    /**
     * 维护中
     */
    MAINTENANCE(2);

    @EnumValue
    private final int code;

    public static Boolean exists(int code) {
        for (StationStatusEnum status : StationStatusEnum.values()) {
            if (status.getCode() == code) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}