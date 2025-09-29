package com.xingchuan.charging.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * 支付的api log类
 */
@Data
public class PayApiLog {
    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 请求/回调地址
     */
    private String apiAddress;
    /**
     * 描述
     */
    private String desc;
    /**
     * 请求的msg
     */
    private String request;
    /**
     * 返回
     */
    private String response;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 调用方向
     * -1, "未知"
     * 0, "我方调用对方"
     * 1, "对方调用我方"
     * 使用CallDirection的elasticName
     * CallDirectionEnum
     */
    private String callDirection;
    /**
     * 支付的信息来源
     * 0, "ALL_IN", "通联"
     * 1, "ALI_PAY", "支付宝"
     * 2, "WECHAT_PAY", "微信支付"
     * PayApiSourceEnum
     */
    private String payApiSource;
    /**
     * 错误日志
     */
    private String errorMsg;
    /**
     * 创建时间
     */
    private LocalDateTime createTime = LocalDateTime.now();
}
