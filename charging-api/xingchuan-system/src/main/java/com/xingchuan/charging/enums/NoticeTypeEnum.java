package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum NoticeTypeEnum {
    NOTICE("1", "通知"),
    ANNOUNCEMENT("2", "公告"),
    ARTICLE("3", "文章");

    private final String code;
    private final String desc;

    public static NoticeTypeEnum getEnumByCode(String code) {
        for (NoticeTypeEnum e : NoticeTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        for (NoticeTypeEnum e : NoticeTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
} 