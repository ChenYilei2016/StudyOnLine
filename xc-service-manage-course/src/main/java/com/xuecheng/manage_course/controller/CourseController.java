package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.Response;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import com.xuecheng.manage_course.service.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/21- 16:25
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    TeachplanService teachplanService;
    @Autowired
    CourseService courseService;

    //组合列表
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return teachplanService.findTeachplanList(courseId);
    }
    //计划增加
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        teachplanService.addTeachplan(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //得到课程列表
    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult getCourseAll(@PathVariable("page")int page,
                                            @PathVariable("size") int size,
                                            CourseListRequest courseListRequest) {
        List<CourseInfo> result = courseService.getCourseAll(page,size,courseListRequest);
        return new QueryResponseResult(CommonCode.SUCCESS,result,0);
    }

    @Override
    @GetMapping("/category/list")
    public CategoryNode getCategoryAll() {
        return courseService.getCategoryAll();
    }


    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase){
        courseService.addCourseBase(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

}