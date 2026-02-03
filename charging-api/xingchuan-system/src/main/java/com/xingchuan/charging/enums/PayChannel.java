package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Objects;

/**
 * 支付渠道
 */
@Getter
@AllArgsConstructor
public enum PayChannel {
    UNKNOWN("UNKNOWN", "未知"),
    WECHAT_PAY("WECHAT_PAY", "微信支付"),
    ALI_PAY("ALI_PAY", "支付宝支付"),
    ALLIN_PAY("ALLIN_PAY", "通联支付"),
    UNION_PAY("UNION_PAY", "银联支付"),
    YEE_PAY("YEE_PAY", "易宝支付"),
    ;

    @EnumValue
    private final String code;
    private final String desc;

    public static PayChannel getByCode(String code) {
        for (PayChannel value : PayChannel.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static Payment toPayment(PayChannel channel) {
        switch (channel) {
            case WECHAT_PAY:
                return Payment.WECHAT_PAY;
            case ALI_PAY:
                return Payment.ALI_PAY;
            case ALLIN_PAY:
                return Payment.ALLIN_PAY;
            case UNION_PAY:
                return Payment.UNION_PAY;
            case YEE_PAY:
                return Payment.YEE_PAY;
            default:
                return Payment.UNKNOWN;
        }

    }
}
