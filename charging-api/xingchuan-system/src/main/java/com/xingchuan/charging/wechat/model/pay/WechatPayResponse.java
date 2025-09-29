package com.xingchuan.charging.wechat.model.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信支付返回实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPayResponse {
    /**
     * 时间戳 string(32)
     * 标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数。
     * 注意：部分系统取到的值为毫秒级，商户需要转换成秒(10位数字)。
     */
    @JsonProperty("timeStamp")
    private String timeStamp;
    /**
     * string(32)
     * 【随机字符串】不长于32位。该值建议使用随机数算法生成。
     */
    @JsonProperty("nonceStr")
    private String nonceStr;
    /**
     * package 必填 string(128)
     * 【预支付交易会话标识】小程序下单接口返回的prepay_id参数值
     * 提交格式如：prepay_id=***
     */
    @JsonProperty("package")
    private String packageVal;
    /**
     * 签名类型
     * 必填 string(32)
     * 【签名类型】默认为RSA，仅支持RSA。
     */
    @JsonProperty("signType")
    private String signType = "RSA";
    /**
     * 签名值必填 string(512)
     * <p>
     * 使用字段appid、timeStamp、nonceStr、package计算得出的签名值，
     * 详细参考：小程序调起支付签名
     * 注意：签名所使用的appid，为【小程序下单】时传入的sp_appid或sub_appid，
     * 微信支付会校验下单与调起支付所使用的appid的一致性。
     */
    @JsonProperty("paySign")
    private String paySign;
}
