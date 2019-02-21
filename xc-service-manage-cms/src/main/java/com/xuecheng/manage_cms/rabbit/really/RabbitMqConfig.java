package com.xuecheng.manage_cms.rabbit.really;

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
    public Exchange exchangeForCms(){
        return new DirectExchange(rabbitMqProperties.getExchangeName());
    }

}
