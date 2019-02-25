package com.xuecheng.manage_course.dao;

import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.mongoDao.SysRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    SysRepository sysRepository;
    @Test
    public void testCourseBaseRepository(){
        System.out.println(courseMapper.findCourseInfoList(null));

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }
    @Test
    public void testFindTeachplan(){
        SysDictionary sysDictionary= new SysDictionary();
        sysDictionary.setId("5a7d50bdd019f150f4ab8ef7");
        List<SysDictionary> all = sysRepository.findAll(Example.of(sysDictionary));

        System.out.println("");
    }

    public static void main(String[] args) {
        String imgPath = "group1/M00/00/00/rBsACFxyNqKAVz8EAAfvaiiDjBU796.bmp";
        String group = StringUtils.substringBefore(imgPath, "/");
        String path = StringUtils.substringBetween(imgPath, "/", ".");
        String after = StringUtils.substringAfter(imgPath, ".");
        System.out.println( after);
    }
}
