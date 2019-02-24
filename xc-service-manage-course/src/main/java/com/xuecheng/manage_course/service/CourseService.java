package com.xuecheng.manage_course.service;

import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.PicRepository;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/23- 16:16
 */
@Service
@Transactional
public class CourseService {
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    PicRepository picRepository;

//    id:'4028e581617f945f01617f9dabc40000',
//    name:'bootstrap',
//    pic:''
    public List<CourseInfo> getCourseAll(int page, int size, CourseListRequest courseListRequest) {
        PageHelper.startPage(page,size);
        List<CourseInfo> result= courseMapper.findCourseInfoList(courseListRequest);
        return result;
    }

    public CategoryNode getCategoryAll() {
        return courseMapper.findCategoryNode();
    }

    //增加一门课程: 添加一条计划
    public void addCourseBase(CourseBase courseBase){
        //添加课程
        CourseBase save = courseBaseRepository.save(courseBase);
        //添加根课程计划
        Teachplan teachplan = new Teachplan();
        teachplan.setParentid("0");
        teachplan.setGrade(save.getGrade().substring(5));
        teachplan.setCourseid(save.getId());
        teachplan.setOrderby(1);
        teachplan.setStatus("0");
        teachplan.setPname(save.getName());
        teachplanRepository.save(teachplan);
        //乱加一张图片用于显示
        CoursePic coursePic = new CoursePic();
        coursePic.setCourseid(save.getId());
        coursePic.setPic("null");
        picRepository.save(coursePic);

    }
}
