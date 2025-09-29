package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallDirectionEnum {

    /**
     * 未知
     */
    UNKNOWN(-1, "UNKNOWN", ""),
    /**
     * 我方主动调用对方接口
     */
    OUTGOING(0, "OUTGOING", "我方调用对方"),

    /**
     * 对方回调我们接口
     */
    INCOMING(1, "INCOMING", "对方调用我方"),
    ;
    private final Integer code;
    private final String name;
    private final String desc;
}
