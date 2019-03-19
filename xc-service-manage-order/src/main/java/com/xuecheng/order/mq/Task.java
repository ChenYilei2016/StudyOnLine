package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.dao.XcTaskRepository;
import com.xuecheng.order.service.XcTaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/18- 16:08
 */
@Component
public class Task {
    @Autowired
    private XcTaskService xcTaskService;
    @Autowired
    XcTaskRepository xcTaskRepository;

    //扫描数据库 "加入课程(learing)" 任务
    @Scheduled(fixedDelay = 60000) //每隔一分钟秒调度 cron = "* 0/60 * * * *",
    public void chooseCourseTask() throws InterruptedException {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE,-1);
        // 找到一分钟前更新的任务 再一次进行发布
        List<XcTask> xcTaskBeforeTime = xcTaskService.findXcTaskBeforeTime(calendar.getTime());

        //发布每一个任务
        for (XcTask xcTask : xcTaskBeforeTime) {
            xcTaskService.publicTask(xcTask);
        }
    }

    //结束 分布式事务
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE})
    public void accpet_finish_task(XcTask xcTask){
        xcTaskService.accpet_finish_task(xcTask);
    }
}
