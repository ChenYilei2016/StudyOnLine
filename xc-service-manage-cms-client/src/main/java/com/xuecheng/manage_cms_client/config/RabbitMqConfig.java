package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/20- 19:30
 */
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfig {
    @Autowired
    RabbitMqProperties rabbitMqProperties;

    @Bean
    public Queue queueForCms(){
        return new Queue(rabbitMqProperties.getQueue());
    }
    @Bean
    public Exchange exchangeForCms(){
        return new DirectExchange(rabbitMqProperties.getExchangeName());
    }
    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(queueForCms())
                .to(exchangeForCms())
                .with(rabbitMqProperties.getRoutingKey()).noargs();
    }
}
