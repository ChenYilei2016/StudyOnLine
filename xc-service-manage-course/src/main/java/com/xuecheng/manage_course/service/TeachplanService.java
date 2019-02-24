package com.xuecheng.manage_course.service;

import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/21- 17:21
 */
@Service
public class TeachplanService {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    TeachplanRepository teachplanRepository;


    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId){
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }

    //增加一级根节点
    public void addRootTeachplan(Teachplan teachplan) {

    }
    //增加一个计划 判断有无父ID
    public void addTeachplan(Teachplan teachplan) {
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(courseid)||
            StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //parentId 为空,应该是创建2级节点
        if(StringUtils.isEmpty(parentid)){
            //先找到一级节点的ID,根据courseId和grade='1'找
            Teachplan temp = new Teachplan();
            temp.setCourseid(courseid);
            temp.setGrade("1");
            Teachplan parentTeachplane = teachplanRepository.findOne(Example.of(temp)).get();
            //创建2级节点
            BeanUtils.copyProperties(teachplan,temp);
            temp.setGrade("2");
            temp.setParentid(parentTeachplane.getId());
            teachplanRepository.save(temp);
        }else{
            //创建3级节点
            teachplan.setGrade("3");
            teachplanRepository.save(teachplan);
        }

    }
}
