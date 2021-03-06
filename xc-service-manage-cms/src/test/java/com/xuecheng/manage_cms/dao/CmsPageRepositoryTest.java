package com.xuecheng.manage_cms.dao;

import com.mongodb.MongoClient;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/15- 11:17
 */
@SpringBootTest(value = "com.xuecheng.manage_cms")
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;


    @Test
    public void testFindAll(){
        Page<CmsPage> all = cmsPageRepository.findAll(PageRequest.of(0, 1));
        System.err.println(all.getContent());
    }

    @Test
    public void testaLL(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("轮播");
        ExampleMatcher matching = ExampleMatcher.matching();
        //设置模糊查询
        ExampleMatcher exampleMatcher = matching.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        System.out.println(cmsPageRepository.findAll(Example.of(cmsPage,exampleMatcher)));
    }
}