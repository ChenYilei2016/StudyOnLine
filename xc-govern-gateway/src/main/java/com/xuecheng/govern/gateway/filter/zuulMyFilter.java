package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/12- 20:45
 */
@Component
public class zuulMyFilter extends ZuulFilter {
    @Autowired
    StringRedisTemplate redisTemplate;
    public HttpServletRequest request(){
        return RequestContext.getCurrentContext().getRequest();
    }
    public HttpServletResponse response(){
        return RequestContext.getCurrentContext().getResponse();
    }
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    public static final String AUTH_HEADER = "Authorization";
    public static final String UNAUTHENTICATED_Body ;
    static{
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        UNAUTHENTICATED_Body = JSONObject.toJSONString(responseResult);
    }
    @Override
    public Object run() throws ZuulException {
        //检验header
        String header = request().getHeader(AUTH_HEADER);
        if(StringUtils.isBlank(header)|| !StringUtils.startsWith(header,"Bearer ")){
            System.err.println("检验header"+header);
            return accessDenied();
        }
        //检验cookie
        String cookieValue = CookieUtil.readCookie(request(), "uid").get("uid");
        if(StringUtils.isBlank(cookieValue)){
            System.err.println("检验cookie"+cookieValue);
            return accessDenied();
        }
        //检查redis中有无 cookie所持有的令牌
        String realHeader = header.substring(7);
        Long expire = redisTemplate.getExpire("user_token:" + cookieValue);
        if(0 > expire){
            System.err.println("检查redis中有无 cookie所持有的令牌"+realHeader);
            return accessDenied();
        }

        return null;
    }
    //拒绝服务
    private Object accessDenied(){
        RequestContext currentContext = RequestContext.getCurrentContext();
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseStatusCode(200);
        currentContext.setResponseBody(UNAUTHENTICATED_Body);
        currentContext.getResponse().setContentType("application/json;charset=utf-8");
        return null;
    }
}
