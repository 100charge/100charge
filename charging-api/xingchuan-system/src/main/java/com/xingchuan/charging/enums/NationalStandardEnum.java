package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * 国家标准
 */
@Getter
@AllArgsConstructor
public enum NationalStandardEnum {

    /**
     * 对应2011标准
     */
    STANDARD_2011(1, "2011"),

    /**
     * 对应2015标准
     */
    STANDARD_2015(2, "2015");

    private final Integer code;
    private final String description;

    public static String getNationalStandard(Integer code) {
        for (NationalStandardEnum standard : NationalStandardEnum.values()) {
            if (standard.code.equals(code)) {
                return standard.description;
            }
        }
        return "未知";
    }
} 