package com.xingchuan.charging.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    private List<Config> configs;

    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;

        /**
         * 跳转地址
         */
        private String page;

        /**
         * 小程序版本
         */
        private String envVersion;

        /**
         * sendMessage 发送订阅消息 POST
         */
        private String wxSendMessageUrl;

        /**
         * 启动充电提示模板
         */
        private String startChargingNotificationTemplate;

        /**
         * 停止充电提示模板
         */
        private String stopChargingNotificationTemplate;

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

    }

}