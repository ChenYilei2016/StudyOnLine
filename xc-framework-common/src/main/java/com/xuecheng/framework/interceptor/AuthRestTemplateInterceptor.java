package com.xuecheng.framework.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/17- 12:52
 */
public class AuthRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            //自己的token request
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest headerRequest = requestAttributes.getRequest();
            Enumeration<String> headerNames = headerRequest.getHeaderNames();

            while(headerNames.hasMoreElements()){
                String name = headerNames.nextElement();
                String header = headerRequest.getHeader(name);
                request.getHeaders().add(name,header);
            }

        }catch (Exception e){

        }
        return execution.execute(request,body);
    }
}
