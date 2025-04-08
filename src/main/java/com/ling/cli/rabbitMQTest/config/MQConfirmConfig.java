package com.ling.cli.rabbitMQTest.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MQConfirmConfig implements ApplicationContextAware {
    private final RabbitTemplate rabbitTemplate;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            // 配置回调
            log.debug("exchange:{},message:{}, Code:{}, Text:{}, key:{}",
                    returnedMessage.getExchange(), returnedMessage.getMessage(), returnedMessage.getReplyCode(),
                    returnedMessage.getReplyText(), returnedMessage.getRoutingKey());
        });
    }
}