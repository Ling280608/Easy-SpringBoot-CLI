package com.ling.cli.rabbitMQTest.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 大黑
 * @Date: 2024/3/29 0:07
 */
@Component
@Slf4j
public class ListenerTest {

    @RabbitListener(queues = "simple.queue")
    public void listenerMq(String queueMsg) {
        // System.out.println("消费者收到了消息：" + queueMsg);
        log.info("接收到的消息为{}", queueMsg);
    }

    // /**
    //  * work模式下的接收
    //  *
    //  * @param queueMsg
    //  * @throws InterruptedException
    //  */
    // @RabbitListener(queues = "work.queue")
    // public void workQueueMq1(String queueMsg) throws InterruptedException {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("消费者1" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(20);
    // }
    //
    // @RabbitListener(queues = "work.queue")
    // public void workQueueMq2(String queueMsg) throws InterruptedException {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("消费者2" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(200);
    // }
    //
    // /**
    //  * fanout交换机模式下的接收
    //  *
    //  * @param queueMsg
    //  * @throws InterruptedException
    //  */
    // @RabbitListener(queues = "fanout.queue1")
    // public void fanoutQueueMq1(String queueMsg) throws InterruptedException {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("fanoutQueue1" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(20);
    // }
    //
    // @RabbitListener(queues = "fanout.queue2")
    // public void fanoutQueueMq2(String queueMsg) throws InterruptedException {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("fanoutQueue2" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(200);
    // }
    //
    // /**
    //  * direct交换机模式下的接收
    //  *
    //  * @param queueMsg
    //  */
    // @RabbitListener(queues = "direct.queue1")
    // public void directQueueMq1(String queueMsg) {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("directQueueMq1" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(20);
    // }
    //
    // @RabbitListener(queues = "direct.queue2")
    // public void directQueueMq2(String queueMsg) {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("directQueueMq2" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(200);
    // }
    //
    // /**
    //  * direct交换机模式下的接收(基于注解模式进行接收)
    //  *
    //  * @param queueMsg
    //  */
    // @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.queue1", durable = "true"), exchange = @Exchange(name = "dahei.direct", type = ExchangeTypes.DIRECT), // 交换机类型type，有枚举类可供选择，这里一般都会有默认值
    //         key = {"red", "blue"}// direct交换机key，数组类型，可以声明多个key
    // ))
    // public void directNewQueueMq1(String queueMsg) {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("directQueueMq1" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(20);
    // }
    //
    // @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.queue2", durable = "true"), exchange = @Exchange(name = "dahei.direct", type = ExchangeTypes.DIRECT), // 交换机类型type，有枚举类可供选择，这里一般都会有默认值
    //         key = {"yellow", "blue"}// direct交换机key，数组类型，可以声明多个key
    // ))
    // public void directNewQueueMq2(String queueMsg) {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("directQueueMq2" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(200);
    // }
    //
    // /**
    //  * topic交换机模式
    //  *
    //  * @param queueMsg
    //  */
    // @RabbitListener(queues = "topic.queue1")
    // public void topicQueueMq1(String queueMsg) {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("topicQueueMq1" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(20);
    // }
    //
    // @RabbitListener(queues = "topic.queue2")
    // public void topicQueueMq2(String queueMsg) {
    //     // System.out.println("消费者收到了消息：" + queueMsg);
    //     log.info("topicQueueMq2" + "接收到的消息为{}", queueMsg);
    //     // Thread.sleep(200);
    // }
}