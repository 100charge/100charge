package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 地区枚举类
 */
@Getter
@AllArgsConstructor
public enum BannerEnum {

    /**
     * 关闭（不显示）
     */
    NO(0),
    /**
     * 开启（显示）
     */
    YES(1);


    @EnumValue
    private final Integer code;
}
