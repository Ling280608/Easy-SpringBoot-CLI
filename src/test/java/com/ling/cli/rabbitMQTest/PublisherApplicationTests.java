package com.ling.cli.rabbitMQTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@SpringBootTest
class PublisherApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() throws InterruptedException {
        String queueName = "simple.queue";
        String queueMsg = "你好，mq，这是伽有";
        while (true) {
            rabbitTemplate.convertAndSend(queueName, queueMsg);
            Thread.sleep(1000);
        }
    }

    @Test
        // 发送到队列
    void workQueue1() throws InterruptedException {
        // 队列名称
        String queueName = "work.queue";
        for (int i = 0; i < 50; i++) {
            // 发送的消息
            String queueMsg = "你好，workQueue_" + i;
            rabbitTemplate.convertAndSend(queueName, queueMsg);
            Thread.sleep(20);
        }
    }

    @Test
    void fanoutQueue() {
        // 发送到fanout交换机
        String exeChange = "dahei.fanout";
        String queueMsg = "helle,tow queue";
        rabbitTemplate.convertAndSend(exeChange, null, queueMsg);
    }

    @Test
    void directQueue() {
        // 发送到direct交换机
        String exeChange = "dahei.fanout";
        String queueMsg = "helle,tow queue";
        String routKet = "red";
        rabbitTemplate.convertAndSend(exeChange, routKet, queueMsg);
    }

    @Test

    void topicQueue() {
        // 发送到topic交换机
        String exeChange = "dahei.topic";
        String queueMsg = "helle,tow queue";
        String routKet = "china.dahei";
        rabbitTemplate.convertAndSend(exeChange, routKet, queueMsg);
    }


    @Test
    void objectQueue() {
        // 发送其他类型的数据
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("name", "大黑");
        map.put("age", 22);
        rabbitTemplate.convertAndSend("object.queue", map);
    }

    // 回调消息测试
    @Test
    void returnMessage() throws InterruptedException {
        // 创建cd（callback）
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        // 添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("回调消息失败", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                log.debug("收到confirm back，开始发送消息");
                if (result.isAck()) {
                    log.debug("消息发送成功, 收到ack");
                } else {
                    log.debug("消息发送失败，收到nack,原因{}", result.getReason());
                }
            }
        });
        rabbitTemplate.convertAndSend("dahei.direct", "reds", "hello 大黑 回调情况如何", cd);
        Thread.sleep(2000);
    }

    // 在临时消息发送达到内存最大时，会将一部分消息转存在磁盘中，然后会造成消息阻塞的情况
    @Test
    void pageOutTest() {
        Message message = MessageBuilder.withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();// 创建一个非持久化的消息发送
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("simple.queue", message);
        }
    }
}