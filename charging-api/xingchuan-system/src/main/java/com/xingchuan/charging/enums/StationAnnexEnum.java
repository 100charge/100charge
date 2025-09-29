package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 场站附件类型枚举类
 */
@Getter
@AllArgsConstructor
public enum StationAnnexEnum {

    /**
     * logo
     */
    LOGO(1),

    /**
     * 场站图片
     */
    STATION_IMAGE(2),

    /**
     * 营业执照
     */
    BUSINESS_LICENSE(3);


    @EnumValue
    private final int code;

}