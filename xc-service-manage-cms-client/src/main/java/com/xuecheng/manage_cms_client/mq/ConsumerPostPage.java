package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听MQ，接收页面发布消息
 * @author Administrator
 * @version 1.0
 **/
@Component
public class ConsumerPostPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);
    @Autowired
    PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(Map map){
        try {
            Object pageId = map.get("pageId");
            pageService.savePageToLocal(pageId.toString());
            System.out.println("处理成功");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
