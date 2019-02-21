package com.xuecheng.manage_cms.rabbit.test;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.annotation.Resources;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/20- 15:23
 */

public class RabbitConfig {
    // * 不能为空
    // # 可以为空且多个值
    // xc.#.sms.#
    // xc.#.email.#
    // xc.sms.email 可以匹配以上2种情况
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    public static final String ROUTINGKEY_EMAIL="inform.#.email.#";
    public static final String ROUTINGKEY_SMS="inform.#.sms.#";

    //交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){

        return new TopicExchange(EXCHANGE_TOPICS_INFORM,true,false);
    }
    //队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    //绑定
    @Bean
    public Binding binding(
            @Qualifier(value = EXCHANGE_TOPICS_INFORM) Exchange ex,
            @Qualifier(QUEUE_INFORM_EMAIL) Queue queue
    ){
        return BindingBuilder.bind(queue).to(ex).with(ROUTINGKEY_EMAIL).noargs();
    }
    @Bean
    public Binding binding2(
            @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange ex,
            @Qualifier(QUEUE_INFORM_SMS) Queue queue
    ){
        return BindingBuilder.bind(queue).to(ex).with(ROUTINGKEY_SMS).noargs();
    }

}
