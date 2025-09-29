package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 地区枚举类
 */
@Getter
@AllArgsConstructor
public enum AreaEnum {

    /**
     * 隐藏
     */
    NO(0),
    /**
     * 展示
     */
    YES(1);


    @EnumValue
    private final Integer code;
}
