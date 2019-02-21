package com.xuecheng.manage_cms.rabbit.really;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/20- 19:30
 */

@ConfigurationProperties(prefix = "xuecheng.mq")
@Data
public class RabbitMqProperties {
    //唯一标识队列
    private String queue;
    //用来指定此客户端是什么站点的服务器
    private String routingKey;
    //交换机名称
    private String exchangeName = "ex_routing_cms_postpage";

}
