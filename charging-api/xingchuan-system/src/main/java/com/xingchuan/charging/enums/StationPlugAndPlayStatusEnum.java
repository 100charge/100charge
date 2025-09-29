package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站-是否即插即冲状态
 */
@Getter
@AllArgsConstructor
public enum StationPlugAndPlayStatusEnum {

    /**
     * 否
     */
    NO(1),
    /**
     * 是
     */
    YES(1);

    @EnumValue
    private final int code;

    public static Boolean exists(int code) {
        for (StationPlugAndPlayStatusEnum status : StationPlugAndPlayStatusEnum.values()) {
            if (status.getCode() == code) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}