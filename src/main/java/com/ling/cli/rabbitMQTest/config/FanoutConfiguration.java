package com.ling.cli.rabbitMQTest.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 大黑
 * @Date: 2024/4/1 0:36
 */
@Configuration
public class FanoutConfiguration {
    // 首先声明一个交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        // ExchangeBuilder.fanoutExchange("dahei.fanout1").build();这种创建方式跟new创建是一样的
        return new FanoutExchange("dahei.fanout1");
    }
 
    // 声明一个队列
    @Bean
    public Queue queue() {
        // QueueBuilder.durable()（创建一个持久的队列，将消息写入磁盘中）;这种创建方式跟new是一样的
        // return QueueBuilder.durable("fanout.queue3").lazy().build();(创建一个lazy Queue-惰性队列)
        return new Queue("simple.queue");// 默认持久的
    }
    // 绑定交换机和队列
    @Bean
    public Binding binding(FanoutExchange fanoutExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
    // 在绑定交换机和队列的时候还可以直接调用这个方法
    // 这中方法和上面那种的作用是一样的
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(fanoutExchange());
//    }
}