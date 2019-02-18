package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-13 10:07
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    public static final String BASEURL= System.getProperty("user.dir")+"/src/test/resources/templates/";

    //基于模板生成静态化文件
    // .ftl == > .html
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());

//        System.err.println(new File(System.getProperty("user.dir")+"/src/test/resources/templates/test1.ftl").getAbsolutePath());
        System.err.println(FreemarkerTest.class.getResource("/").getPath());

        configuration.setDirectoryForTemplateLoading(new File(BASEURL));
        Template template = configuration.getTemplate("test1.ftl");
//        String str = FreeMarkerTemplateUtils.processTemplateIntoString(template, getMap());
//        Files.copy(new ByteArrayInputStream(str.getBytes()), new File(BASEURL + "demo.html").toPath());
        template.process(getMap(),new FileWriter(new File(BASEURL + "demo.html")));
    }

    //基于模板字符串生成静态化文件
    // "<a></a>" ==> .html
    @Test
    public void testGenerateHtmlByString() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        //模板内容，这里测试时使用简单的字符串作为模板
        String templateString="" +
                "<html>\n" +
                "    <head></head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" +
                "</html>";
        Template template = new Template("template",templateString,configuration);
        template.setOutputEncoding("utf-8");
//        System.out.println(FreeMarkerTemplateUtils.processTemplateIntoString(template,getMap()) );
        template.process(getMap(),new FileWriter(new File(BASEURL + "demo.html")));

    }

    //数据模型
    private Map getMap(){
        Map<String, Object> map = new HashMap<>();
        //向数据模型放数据
        map.put("name","黑马程序员");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMondy(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMondy(200.1f);
        stu2.setAge(19);
//        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus",stus);
        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        //向数据模型放数据
        map.put("stu1",stu1);
        //向数据模型放数据
        map.put("stuMap",stuMap);
        return map;
    }
}
