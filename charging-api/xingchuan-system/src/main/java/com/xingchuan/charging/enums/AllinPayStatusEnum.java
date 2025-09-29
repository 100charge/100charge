package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * app用户 通联状态
 */
@Getter
@AllArgsConstructor
public enum AllinPayStatusEnum {

    CREATE_USER(0, "创建会员"),
    BIND_PAYMENT_ACCOUNT(1, "绑定支付账户"),
    BIND_SUCCESS(2, "绑定成功");

    @EnumValue
    private final Integer code;
    private final String desc;
}