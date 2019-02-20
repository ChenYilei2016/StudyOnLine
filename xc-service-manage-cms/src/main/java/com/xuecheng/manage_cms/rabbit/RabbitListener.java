package com.xuecheng.manage_cms.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/20- 15:52
 */
//@Component
public class RabbitListener {
    @org.springframework.amqp.rabbit.annotation.RabbitListener(
        queues = {RabbitConfig.QUEUE_INFORM_EMAIL}
    )
    public void email(String msg,Message message){
        System.out.println(message.getBody());
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(
            queues = {RabbitConfig.QUEUE_INFORM_SMS}
    )
    public void sms(String msg,Message message){
        System.out.println(message.getBody());
    }
}
