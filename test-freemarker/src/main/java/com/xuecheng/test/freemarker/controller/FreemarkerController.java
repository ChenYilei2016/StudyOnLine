package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-12 18:40
 **/
@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {
    @Autowired
    RestTemplate restTemplate;



    @RequestMapping("/test1")
    public String freemarker(Map<String, Object> map){
        //向数据模型放数据
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
        map.put("name","黑马程序员");////////////////////////////////////////
        //向数据模型放数据
        map.put("stus",stus);//////////////////////////////////////////////////
        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        //向数据模型放数据
        map.put("stu1",stu1);//////////////////////////////////////////////////
        //向数据模型放数据
        map.put("stuMap",stuMap);//////////////////////////////////////////////////
        //返回模板文件名称
        return "test1";
    }

    //http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f
    @RequestMapping("/banner")
    public String index_banner(Map<String, Object> map){
       //获取model的信息
        Map forObject = restTemplate.getForObject("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        map.put("model",forObject);
        System.out.println(forObject);
        return "index_banner";
    }
}
