package com.xingchuan.charging.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订单自动支付生产者
 */
@Slf4j
@Component
public class OrderBillProducer {

    /**
     * 连接符
     */
    private static final String TOPIC_TAG_FORMAT = "%s:%s";
    /**
     * topic主题
     */
    @Value("${rocketmq.topic}")
    private String TOPIC;
    /**
     * 消息tag
     */
    @Value("${rocketmq.tags.order-bill}")
    private String TAG;

    @Resource
    private RocketMQTemplate template;

    /**
     * 发送账单
     *
     * @param msgBody 消息实体/账单号
     */
    public void sendBill(String msgBody) {
        if (StringUtils.isEmpty(TOPIC) || StringUtils.isEmpty(TAG)) {
            log.error("RocketMQ 主题或标签配置为空，无法发送消息");
            return;
        }
        String destination = String.format(TOPIC_TAG_FORMAT, TOPIC, TAG);
        Message<String> message = MessageBuilder.withPayload(msgBody).build();
        try {
            // 延迟1S发送
            SendResult sendResult = template.syncSend(destination, message, 3000, 1);
            log.info("账单消息发送成功，消息体：{}，返回值：{}", msgBody, sendResult);
        } catch (Exception e) {
            log.error("发送账单失败,消息体：{}：错误信息：{}", msgBody, e.getMessage());
        }
    }
}
