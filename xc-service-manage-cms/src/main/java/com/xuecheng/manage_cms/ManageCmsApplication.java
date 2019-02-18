package com.xuecheng.manage_cms;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/14- 21:49
 */
@SpringBootApplication
//@EntityScan(basePackages = {"com.xuecheng.framework.domain.cms"}) //扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"}) //扫描接口 TODO 查看区别
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"})//自己包下的
@ComponentScan(basePackages = {"com.xuecheng.framework"})//common等包的异常捕获器
public class ManageCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class,args);
    }
}
