package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站是否展示枚举
 */
@Getter
@AllArgsConstructor
public enum StationShowStatusEnum {

    /**
     * 否
     */
    NO(0),
    /**
     * 是
     */
    YES(1);

    @EnumValue
    private final int code;

    public static Boolean exists(int code) {
        for (StationShowStatusEnum status : StationShowStatusEnum.values()) {
            if (status.getCode() == code) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static int getByCode(int code) {
        return code == NO.code ? NO.code : YES.code;
    }

}