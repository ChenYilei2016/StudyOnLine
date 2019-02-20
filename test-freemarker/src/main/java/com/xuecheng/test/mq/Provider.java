package com.xuecheng.test.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/20- 11:17
 */
public class Provider {
    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("132.232.117.84");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        connectionFactory.setVirtualHost("/leyou");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // * 不能为空
        // # 可以为空且多个值
        // xc.#.sms.#
        // xc.#.email.#
        // xc.sms.email 可以匹配以上2种情况

        channel.basicPublish("ly.item.exchange","test",null,"1+1".getBytes());
//        channel.basicConsume()
        channel.close();
        connection.close();


    }
}
