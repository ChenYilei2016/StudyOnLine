package com.xuecheng.manage_course;

import com.xuecheng.framework.interceptor.AuthFeignInterceptor;
import com.xuecheng.framework.interceptor.AuthRestTemplateInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.course")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.manage_course"})
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common下的所有类
@EnableEurekaClient
@EnableFeignClients
public class ManageCourseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageCourseApplication.class, args);
    }
    @Bean
    public AuthFeignInterceptor authFeignInterceptor(){
        return new AuthFeignInterceptor();
    }

    @Bean
    public AuthRestTemplateInterceptor authRestTemplateInterceptor(){
        return new AuthRestTemplateInterceptor();
    }

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        restTemplate.setInterceptors(Arrays.asList(authRestTemplateInterceptor()));
        return restTemplate;
    }

}