package com.xingchuan.charging.wechat.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额工具类
 */
public class AmountUtil {
    /**
     * 将元转换成分
     *
     * @param amount 金额（元），精确到小数点后两位
     */
    public static Long convertY2Cent(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_UP).longValue();
    }

    /**
     * 将分转换成元
     *
     * @param amount 金额（分）
     */
    public static BigDecimal convertY2CNY(Long amount) {
        return new BigDecimal(amount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
