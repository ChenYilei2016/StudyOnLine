package com.xuecheng.manage_cms.service;

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
 * @date 2019/02/19- 14:28
 */
@SpringBootTest(value = "com.xuecheng.manage_cms")
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    PageService pageService;

    @Test
    public void getPageHtml() {
        String pageHtml = pageService.getPageHtml("5a795ac7dd573c04508f3a56");
        System.out.println(pageHtml);
    }
}