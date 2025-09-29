package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站-是否优选状态
 */
@Getter
@AllArgsConstructor
public enum StationPreferredStatusEnum {

    /**
     * 否
     */
    NOT_PREFERRED(0),
    /**
     * 优选
     */
    PREFERRED(1);

    @EnumValue
    private final int code;

    public static Boolean exists(int code) {
        for (StationPreferredStatusEnum status : StationPreferredStatusEnum.values()) {
            if (status.getCode() == code) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}