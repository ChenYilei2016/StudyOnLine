package com.xuecheng.manage_course.config;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/10- 13:28
 */

import feign.RequestTemplate;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * 客户端 持有公钥的配置
 *
 * key: -----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled  =  true,  securedEnabled  =  true)//激活方法上的PreAuthorize注解
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {

    //公钥
    private  static  final  String  PUBLIC_KEY  =  "publickey.txt";
    //定义JwtTokenStore，使用jwt令牌
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter)  {
        return  new JwtTokenStore(jwtAccessTokenConverter);
    }
    //定义JJwtAccessTokenConverter，使用jwt令牌
    @Bean
    public  JwtAccessTokenConverter  jwtAccessTokenConverter()  {
        JwtAccessTokenConverter  converter  =  new  JwtAccessTokenConverter();
        converter.setVerifierKey(getPubKey());
        converter.setAccessTokenConverter(new MyAccessTokenConverter());
        return  converter;
    }


    /**
     *  获取非对称加密公钥  Key
     *  @return  公钥  Key
     */
    private  String  getPubKey()  {
        Resource resource  =  new ClassPathResource(PUBLIC_KEY);
        try  {
            InputStreamReader inputStreamReader  =  new
                    InputStreamReader(resource.getInputStream());
            BufferedReader br  =  new  BufferedReader(inputStreamReader);
            return  br.lines().collect(Collectors.joining("\n"));
        }  catch  (IOException ioe)  {
            return  null;
        }
    }
    //Http安全配置，对每个到达系统的http请求链接进行校验
    @Override
    public  void  configure(HttpSecurity http)  throws  Exception  {
        //所有请求必须认证通过
        http
                .authorizeRequests()
                .antMatchers("/v2/api-docs","/swagger-resources/configuration/ui",
                        "/swagger-resources","/swagger-resources/configuration/security",
                        "/swagger-ui.html","/webjars/**","/course/coursepic/list/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                ;



    }
}
