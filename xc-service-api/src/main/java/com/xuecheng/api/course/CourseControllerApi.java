package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by Administrator.
 */

@Api(value="课程管理接口",description = "课程管理接口，提供课程的增、删、改、查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("获取我的所有课程列表")
    public QueryResponseResult getCourseAll(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("查询课程分类list")
    public CategoryNode getCategoryAll();

    @ApiOperation("增加一门课程")
    ResponseResult addCourseBase(@RequestBody CourseBase courseBase);

    @ApiOperation("增加课程的图片关联")
    ResponseResult addCoursePic(String courseId,String pic);

    @ApiOperation("查询课程图片")
    CoursePic queryCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("查询课程详情的信息 CMS Dataurl")
    public CourseView getCourseView(String courseId);

    @ApiOperation("获得预览课程的url,必须先创建或更新cmsPage")
    public CoursePublishResult preview(String courseId);

}
