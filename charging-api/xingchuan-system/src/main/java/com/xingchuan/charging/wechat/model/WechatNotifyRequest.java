package com.xingchuan.charging.wechat.model;

import lombok.Data;

/**
 * 通知的请求
 */
@Data
public class WechatNotifyRequest {
    /**
     * 验签的微信支付平台证书序列号/微信支付公钥ID
     */
    private String serial;
    /**
     * 签名中的随机数
     */
    private String nonce;
    /**
     * 验签的签名值
     */
    private String signature;
    /**
     * 签名中的时间戳
     */
    private String timestamp;
    /**
     * HTTP 请求体 body。切记使用原始报文
     */
    private String body;
}
