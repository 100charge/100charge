package com.xingchuan.charging.wechat.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 微信配置类
 */
@Data
@Configuration
@EnableCaching
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "pay.wechat")
public class WeChatPayConfig {
    /**
     * 是服务商在微信开放平台（移动应用）或公众平台（公众号/小程序）上申请的一个唯一标识。
     */
    private String appId;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 商户API私钥路径
     */
    private String privateKeyPath;
    /**
     * 公钥路径
     */
    private String publicKeyPath;
    /**
     * 公钥ID
     */
    private String publicKeyId;
    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber;
    /**
     * 商户APIV3密钥
     */
    private String apiV3Key;
    /**
     * 是否需要分账
     */
    private boolean profitSharing;
    /**
     * 订单支付过期时间，默认30分钟
     */
    private Integer expireMinute = 30;
    /**
     * 是否开启http代理协议
     */
    private boolean httpProxyEnabled;
    /**
     * 代理地址
     */
    private String httpProxyHost;
    /**
     * 代理端口
     */
    private int httpProxyPort;
    /**
     * 读超时
     */
    private int readTimeoutMs;
    /**
     * 连接超时
     */
    private int connectTimeoutMs;
    /**
     * 写超时
     */
    private int writeTimeoutMs;
    /**
     * 是否是平台证书模式
     */
    private boolean platformCertMode;

    /**
     * 充值回调地址
     */
    private String rechargeNotifyUrl;
    /**
     * 退款回调地址
     */
    private String refundNotifyUrl;

    /**
     * 获取支付的配置
     */
    @Bean
    @Lazy
    public com.wechat.pay.java.core.Config getPayConfig() {
        if (platformCertMode) {
            return getRSAAutoCertificateConfigBuilder().build();
        }
        return getRSAAutoBuilder().build();
    }

    @Bean
    @Lazy
    public NotificationConfig getRSAPublicKeyConfig() {
        if (platformCertMode) {
            return getRSAAutoCertificateConfigBuilder().build();
        }
        return getRSAAutoBuilder().build();
    }

    /**
     * 微信支付公钥模式
     */
    private RSAPublicKeyConfig.Builder getRSAAutoBuilder() {
        // 从来没有对接过微信支付的，需要使用公钥证书方式
        return new RSAPublicKeyConfig.Builder()
                .merchantId(merchantId)
                .privateKeyFromPath(privateKeyPath)
                .publicKeyFromPath(publicKeyPath)
                .publicKeyId(publicKeyId)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3Key);
    }

    /**
     * 平台证书支付模式
     */
    private RSAAutoCertificateConfig.Builder getRSAAutoCertificateConfigBuilder() {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                .privateKeyFromPath(privateKeyPath)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3Key);
    }
}
