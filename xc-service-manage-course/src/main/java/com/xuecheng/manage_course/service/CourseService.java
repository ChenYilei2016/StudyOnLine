package com.xuecheng.manage_course.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.ext.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    CoursePicRepository coursePicRepository;
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CoursePubRepository coursePubRepository;
    @Autowired
    CmsPageClient cmsPageClient;
    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    TeachplanMediaPubRepository teachplanMediaPubRepository;

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
        coursePicRepository.save(coursePic);

    }

    public ResponseResult addCoursePic(String courseId, String pic) {
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if(picOptional.isPresent()){
            coursePic = picOptional.get();
        }
        //没有课程图片则新建对象
        if(coursePic == null){
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic queryCoursePic(String courseId) {
        return coursePicRepository.findById(courseId).get();
    }

    public ResponseResult deleteCoursePic(String courseId) {
        long count = coursePicRepository.deleteByCourseid(courseId);
        if(count >0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public CourseView getCourseView(String courseId) {
        CourseView courseView = new CourseView();
        CourseBase courseBase = courseBaseRepository.findById(courseId).get();
        courseView.setCourseBase(courseBase);

        CourseMarket courseMarket = courseMarketRepository.findById(courseId).get();
        courseView.setCourseMarket(courseMarket);

        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        courseView.setTeachplanNode(teachplanNode);

        CoursePic coursePic = coursePicRepository.findById(courseId).get();
        courseView.setCoursePic(coursePic);
        return courseView;
    }

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    public CoursePublishResult preview(String courseId) {
        //构造一个这个课程的 cmsPage,保存获得pageId
        CourseBase courseBase = courseBaseRepository.findById(courseId).get();
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);

        //调用远程服务保存cmspage
        CmsPageResult save = cmsPageClient.save(cmsPage);
        String pageId = save.getCmsPage().getPageId();
        //使用pageId返回预览页面
        return new CoursePublishResult(previewUrl+pageId,CommonCode.SUCCESS);
    }
    public void saveTeachplanMediaPub(String courseId){
        //先删除原本有的 TeachplanMediaPub
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        //批量增加TeachplanMediaPub
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findAllByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();

        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        teachplanMediaPubRepository.saveAll(teachplanMediaPubList);
    }
    public CoursePublishResult publish(String courseId) {
        //构造一个这个课程的 cmsPage,保存获得pageId
        CourseBase courseBase = courseBaseRepository.findById(courseId).get();
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);

        //远程进行保存cmsPage ,并静态化
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        
        //更新课程的状态,已经发布
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);

        /**
         * 组合一个CoursePub对象进入数据库 利于logstash爬取 课程的信息
         */
        CoursePub coursePub = createCoursePub(courseId,courseBase);
        coursePubRepository.save(coursePub);

        /**
         *  组合一个TeachplanMediaPub进入数据库 用于 媒资相关信息的爬取
         */
        saveTeachplanMediaPub(courseId);

        return new CoursePublishResult(cmsPostPageResult.getPageUrl(),CommonCode.SUCCESS);
    }



    private CoursePub createCoursePub(String courseId, CourseBase courseBase) {
        CoursePub coursePub = new CoursePub();
        BeanUtils.copyProperties(courseBase,coursePub);

        //查询课程营销
        Optional<CourseMarket> courseMarket = courseMarketRepository.findById(courseId);
        if(courseMarket.isPresent()){
            BeanUtils.copyProperties(courseMarket.get(),coursePub);
        }
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic, coursePub);
        }
        //设置节点
        coursePub.setTeachplan(JSONObject.toJSONString(teachplanMapper.selectList(courseId)));
        //给logstash使用
        coursePub.setTimestamp(new Date());
        //推出时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        coursePub.setPubTime(simpleDateFormat.format(new Date()));

        return coursePub;
    }

    /**
     * 只有TeachGrade == 3 才可以和 视频关联
     * @param teachplanMedia
     * @return
     */
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        String teachplanId = teachplanMedia.getTeachplanId();
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(teachplanId);
        if(!teachplanOptional.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        Teachplan teachplan = teachplanOptional.get();
        //三级节点可以放视频
        if(! "3".equals(teachplan.getGrade())){
            ExceptionCast.cast(CommonCode.FAIL);
        }

        TeachplanMedia one = null ;
        Optional<TeachplanMedia> teachplanMediaOptional  = teachplanMediaRepository.findById(teachplanMedia.getTeachplanId());
        if(!teachplanMediaOptional.isPresent()){
            one = new TeachplanMedia();
        }else{
            one = teachplanMediaOptional.get();
        }
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        one.setTeachplanId(teachplanMedia.getTeachplanId());

        teachplanMediaRepository.save(teachplanMedia);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
