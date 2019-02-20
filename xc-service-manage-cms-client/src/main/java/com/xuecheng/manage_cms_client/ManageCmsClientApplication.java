package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/20- 19:25
 */

@SpringBootApplication
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common包下的类
@ComponentScan(basePackages={"com.xuecheng.manage_cms_client"})//扫描本项目下的所有类
public class ManageCmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class,args);
    }
}
