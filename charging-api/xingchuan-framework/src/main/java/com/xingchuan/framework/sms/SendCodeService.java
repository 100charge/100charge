package com.xingchuan.framework.sms;

/**
 * 阿里云短信服务Service接口
 *
 * @author 100charhe
 */
public interface SendCodeService {
    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     */
    void sendVerificationCode(String phoneNumber, String randomCode);
}
