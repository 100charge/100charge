package com.xingchuan.charging.wechat.service;

import org.springframework.http.ResponseEntity;

/**
 * 支付通知接口
 */
public interface IPayNotifyService {

    /**
     * 处理微信充值回调
     *
     * @param serial    验签的微信支付平台证书序列号/微信支付公钥ID
     * @param signature 验签的签名值
     * @param timestamp 验签的时间戳
     * @param nonce     验签的随机字符串
     * @param body      请求主体中会包含JSON格式的通知参数
     */
    ResponseEntity<?> handleWechatRechargeNotify(String serial, String signature, String timestamp,
                                                 String nonce, String body);

    /**
     * 处理微信充值回调
     *
     * @param serial    验签的微信支付平台证书序列号/微信支付公钥ID
     * @param signature 验签的签名值
     * @param timestamp 验签的时间戳
     * @param nonce     验签的随机字符串
     * @param body      请求主体中会包含JSON格式的通知参数
     */
    ResponseEntity<?> handleWechatRefundNotify(String serial, String signature, String timestamp,
                                               String nonce, String body);
}
