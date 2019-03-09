package com.xuecheng.manage_course.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/09- 10:42
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseServiceTest {

    @Autowired
    CourseService courseService;

    @Test
    public void saveTeachplanMediaPub() {
        courseService.saveTeachplanMediaPub("4028e581617f945f01617f9dabc40000");
    }
}