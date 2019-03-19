package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/18- 21:33
 */
@Service
@Transactional
public class XcTaskService {
    @Autowired
    private XcTaskRepository xcTaskRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    public List<XcTask> findXcTaskBeforeTime(Date date){
        return xcTaskRepository.findByUpdateTimeBefore(date, PageRequest.of(0,100)).getContent();
    }
    public void publicTask(XcTask xcTask){
        //有多个同时发送任务怎么办? 乐观锁 , 只有更新成功的 xcTask 才能执行发送
        int i = xcTaskRepository.updateTaskVersion(xcTask.getId(), xcTask.getVersion());
        if( i > 0 ){
            rabbitTemplate.convertAndSend(xcTask.getMqExchange(),xcTask.getMqRoutingkey(),xcTask);
            //修改此任务的更新时间
            xcTaskRepository.updateTaskTime(xcTask.getId(),new Date());
        }
    }

    public void accpet_finish_task(XcTask xcTask){
        Optional<XcTask> optionalXcTask = xcTaskRepository.findById(xcTask.getId());
        //将任务删除
        if(optionalXcTask.isPresent()){
            xcTaskRepository.deleteById(xcTask.getId());

            //添加历史
            XcTaskHis xcTaskHis  = new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
        }

    }
}
