package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序用户枚举类
 */
@Getter
@AllArgsConstructor
public enum AppUserEnum {

    /**
     * 微信
     */
    WECHAT(1, "微信"),
    /**
     * 支付宝
     */
    ALIPAY(2, "支付宝"),
    /**
     * 企业管理员
     */
    ENTERPRISE_ADMIN(3, "企业管理员");


    @EnumValue
    private final Integer code;
    private final String name;

    public static String getName(int code) {
        for (AppUserEnum appUserEnum : AppUserEnum.values()) {
            if (appUserEnum.getCode() == code) {
                return appUserEnum.getName();
            }
        }
        return "";
    }


}
