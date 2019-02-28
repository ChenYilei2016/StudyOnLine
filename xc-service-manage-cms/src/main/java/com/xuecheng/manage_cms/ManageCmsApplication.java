package com.xuecheng.manage_cms;

import com.google.common.collect.ImmutableMap;
import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/14- 21:49
 */
//@EntityScan(basePackages = {"com.xuecheng.framework.domain.cms"}) //扫描实体类
@SpringBootApplication
@ComponentScan(basePackages = {"com.xuecheng.api"}) //扫描接口 TODO 查看区别
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"})//自己包下的
@ComponentScan(basePackages = {"com.xuecheng.framework"})//common等包的异常捕获器
@EnableEurekaClient
public class ManageCmsApplication {
    public static void main(String[] args) {
        //
        SpringApplication.run(ManageCmsApplication.class,args);
    }

    @Bean
    @Primary
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
