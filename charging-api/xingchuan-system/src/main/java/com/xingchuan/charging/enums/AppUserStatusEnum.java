package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * app用户 帐号状态
 */
@Getter
@AllArgsConstructor
public enum AppUserStatusEnum {

    /**
     * 正常
     */
    NORMAL(0, "正常"),
    /**
     * 停用
     */
    DISABLED(1, "停用");

    @EnumValue
    private final int code;
    private final String name;

    public static String getName(int code) {
        for (AppUserStatusEnum appUserTypeEnum : AppUserStatusEnum.values()) {
            if (appUserTypeEnum.getCode() == code) {
                return appUserTypeEnum.getName();
            }
        }
        return "";
    }
}