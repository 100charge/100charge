package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序用户余额变动枚举类
 */
@Getter
@AllArgsConstructor
public enum AppUserBalanceRecordEnum {

    RECHARGE(0, "充值"),
    CONSUME(1, "消费"),
    WITHDRAW(2, "提现"),
    ORDER_REFUND(3, "订单退款"),
    BALANCE_ALLOCATION(4, "企业余额分配");

    @EnumValue
    private final Integer code;
    private final String desc;

    public static String getDesc(Integer code) {
        for (AppUserBalanceRecordEnum appUserCarEnum : AppUserBalanceRecordEnum.values()) {
            if (appUserCarEnum.getCode().equals(code)) {
                return appUserCarEnum.desc;
            }
        }
        return null;
    }

}
