package com.xingchuan.common.utils;


import com.xingchuan.common.enums.TradeDeviceTypeEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单工具类
 */
public class OrderNoUtils {

    private static final String RECHARGE_FLAG = "CZ-";
    private static final String WITHDRAWAL_FLAG = "TX-";
    private static final String COLLECTION_FLAG = "DS-";
    private static final String PAYMENT_FLAG = "DF-";
    private static final String OUT_REQUEST_NO_FLG = "ORN";
    private static final String DATETIME_PATTERN = "yyMMddHHmmssSSSS";
    private static final String DATETIME_FORMAT = "yyyyMMddHHmmssSSSS";

    /**
     * 生成互联互通的订单号
     * 订单号最大位数27
     *
     * @param platform 平台组织机构代码
     */
    public static String generateHLHTOrderNo(@NotNull String platform) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return platform + format.format(now);
    }

    /**
     * 生成互联互通的订单号
     * 订单号最大位数27
     *
     * @param gunNo  枪号
     * @param source 来源
     */
    public static String generateHLHTOrderNo(String deviceNo, String gunNo, String source) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        LocalDateTime now = LocalDateTime.now();
        return TradeDeviceTypeEnum.YKC.getCode() +
                format.format(now) +
                getStr(deviceNo, 5) +
                getStr(gunNo, 2) +
                getStr(source, 2);
    }

    /**
     * 生成云快充的订单号
     * 订单号最大位数32
     * 兼容互联互通，需要改成27位 + 00000
     *
     * @param gunNo  枪号
     * @param source 来源
     */
    public static String generateYKCOrderNo(String deviceNo, String gunNo, String source) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        LocalDateTime now = LocalDateTime.now();
        return TradeDeviceTypeEnum.YKC.getCode() +
                format.format(now) +
                getStr(deviceNo, 5) +
                getStr(gunNo, 2) +
                getStr(source, 2) +
                "00000";
    }

    /**
     * 生成特来电交流的订单号
     * 订单号最大位数36
     * 兼容互联互通，需要改成27位 + 000000000
     *
     * @param gunNo  枪号
     * @param source 来源
     */
    public static String generateTELDACOrderNo(String deviceNo, String gunNo, String source) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        LocalDateTime now = LocalDateTime.now();
        return TradeDeviceTypeEnum.TELD_AC.getCode() +
                format.format(now) +
                getStr(deviceNo, 4) +
                getStr(gunNo, 3) +
                getStr(source, 2) +
                "000000000";
    }

    /**
     * 生成特来电直流的订单号
     * 订单号最大位数暂时未限制
     * 兼容互联互通，需要改成27位
     *
     * @param gunNo  枪号
     * @param source 来源
     */
    public static String generateTELDDCOrderNo(String deviceNo, String gunNo, String source) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        LocalDateTime now = LocalDateTime.now();
        return TradeDeviceTypeEnum.TELD_DC.getCode() +
                format.format(now) +
                getStr(deviceNo, 4) +
                getStr(gunNo, 3) +
                getStr(source, 2);
    }

    /**
     * 获取规定长度的字符串（截取输入的后length位）
     *
     * @param input  输入
     * @param length 长度
     */
    private static String getStr(String input, int length) {
        if (input.length() >= length) {
            return input.substring(input.length() - length);
        } else {
            StringBuilder sb = new StringBuilder(input);
            while (sb.length() < length) {
                // 在字符串开头插入零
                sb.insert(0, '0');
            }
            String out = sb.toString();
            return out.substring(out.length() - length);
        }
    }

    /**
     * 生成充值订单号
     *
     * @return 结果
     */
    public static String generateRechargeOrderNo() {
        return RECHARGE_FLAG + getTransactionOrderNo();
    }

    /**
     * 生成提现订单号
     *
     * @return 结果
     */
    public static String generateWithdrawalOrderNo() {
        return WITHDRAWAL_FLAG + getTransactionOrderNo();
    }

    /**
     * 生成代收订单号
     *
     * @return 结果
     */
    public static String generateCollectionOrderNo() {
        return COLLECTION_FLAG + getTransactionOrderNo();
    }

    /**
     * 生成代付订单号
     *
     * @return 结果
     */
    public static String generatePaymentOrderNo() {
        return PAYMENT_FLAG + getTransactionOrderNo();
    }

    /**
     * 获取交易订单编号
     */
    private static String getTransactionOrderNo() {
        String format = DateUtils.dateTimeNow();
        return format + (int) (Math.random() * 900000) + 100000;
    }


    /**
     * 生成代付订单号
     *
     * @return 结果
     */
    public static String generateOutRequestNo() {
        return OUT_REQUEST_NO_FLG + getTransactionOrderNo();
    }

    /**
     * 生成星星充电协议的订单号
     *
     * @param source 设备号
     */
    public static String generateStarOrderNo(String source) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        LocalDateTime now = LocalDateTime.now();
        return TradeDeviceTypeEnum.START.getCode() +
                format.format(now) +
                getStr(source, 2);
    }
}
