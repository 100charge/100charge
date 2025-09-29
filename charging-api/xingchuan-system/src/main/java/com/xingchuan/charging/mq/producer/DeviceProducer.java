package com.xingchuan.charging.mq.producer;

import com.alibaba.fastjson.JSON;
import com.xingchuan.charging.domain.model.DeviceCMDMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 协议通信的生产者
 */
@Slf4j
@Component
public class DeviceProducer {
    /**
     * 连接符
     */
    private static final String SYMBOL = "%s:%s";
    /**
     * topic主题
     */
    @Value("${rocketmq.topic}")
    private String TOPIC;
    /**
     * 消息tag
     */
    @Value("${rocketmq.tags.control}")
    private String TAG;
    /**
     * 直接注入使用，用于发送消息到broker服务器
     */
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送即时消息
     *
     * @param msgBody command消息实体
     */
    public SendResult sendDelayMsg(DeviceCMDMsg msgBody) {
        SendResult sendResult = rocketMQTemplate.syncSend(String.format(SYMBOL, TOPIC, TAG), MessageBuilder.withPayload(msgBody).build());
        log.debug("[sendDelayMsg] sendResult={}", JSON.toJSONString(sendResult));
        return sendResult;
    }
}
