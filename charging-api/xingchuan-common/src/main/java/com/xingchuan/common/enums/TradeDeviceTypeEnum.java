package com.xingchuan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单中设备类型
 */
@Getter
@AllArgsConstructor
public enum TradeDeviceTypeEnum {

    /**
     * 取Y.K.C所在26字母位置的数字相加
     * Y=26
     * K=11
     * C=03
     * Y+K+C=26+11+03=37
     */
    YKC("37", "云快充"),
    /**
     * 取T,L,A所在26字母位置的数字相加
     * T=20
     * L=12
     * A=01
     * T+L+A=20+12+01=33
     */
    TELD_AC("33", "特来电交流"),
    /**
     * 取T.L.D所在26字母位置的数字相加
     * Y=20
     * L=12
     * D=04
     * T+L+D=20+12+04=36
     */
    TELD_DC("38", "特来电直流"),
    /**
     * 取X.X所在26字母位置的数字相加
     * X=24
     * X=24
     * X+X=24+24=48
     */
    START("48", "星星");


    private final String code;
    private final String name;
}
