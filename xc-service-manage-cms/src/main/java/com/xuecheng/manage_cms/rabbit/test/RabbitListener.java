package com.xuecheng.manage_cms.rabbit.test;

import org.springframework.amqp.core.Message;

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
