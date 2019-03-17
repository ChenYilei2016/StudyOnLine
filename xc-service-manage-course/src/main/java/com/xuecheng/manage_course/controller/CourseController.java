package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import com.xuecheng.manage_course.service.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    /**
     * 要得到自己的Id
     * @return
     */
    @Override
//    @PreAuthorize("hasAuthority('course_find_list')")
    @GetMapping("/coursebase/list")
    public QueryResponseResult getCourseByCompanyId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String,String> principal=(Map<String,String>)authentication.getPrincipal();//一个map
//        //是在下输了 日
//        XcOauth2Util xcOauth2Util = new XcOauth2Util();
//        XcOauth2Util.UserJwt userJwtFromHeader = xcOauth2Util.getUserJwtFromHeader(request);

        List<CourseInfo> result = courseService.findCourseByCompanyId(principal.get("companyId"));

        return new QueryResponseResult(CommonCode.SUCCESS,result,0);
    }

    //得到课程列表
    //框架拦截 无权限 ×
    @Override
    @PreAuthorize("hasAuthority('course_find_list')")
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult getCourseAll(@PathVariable("page")int page,
                                            @PathVariable("size") int size,
                                            CourseListRequest courseListRequest) {
        List<CourseInfo> result = courseService.getCourseAll(page,size,courseListRequest);
        return new QueryResponseResult(CommonCode.SUCCESS,result,0);
    }


    //框架不拦截 有权限 √
    @Override
    @GetMapping("/coursepic/list/{courseId}")//course_find_pic
    @PreAuthorize("hasAuthority('course_find_pic')")
    public CoursePic queryCoursePic(@PathVariable("courseId") String courseId) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Cookie cookie = new Cookie("123", "456");
        cookie.setMaxAge(100);
        attributes.getResponse().addCookie(cookie);
        return courseService.queryCoursePic(courseId);
    }

    // 不解除框架的拦截 但有权限 √
    @Override
    @GetMapping("/category/list")//course_get_baseinfo
    @PreAuthorize("hasAuthority('course_get_baseinfo')")
    public CategoryNode getCategoryAll() {
        return courseService.getCategoryAll();
    }


    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase){
        courseService.addCourseBase(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(String courseId, String pic) {
        return courseService.addCoursePic(courseId,pic);
    }



    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView getCourseView(@PathVariable("id") String courseId) {
        return courseService.getCourseView(courseId);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String courseId) {
        return courseService.preview(courseId);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id) {
        return courseService.publish(id);
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }

}
