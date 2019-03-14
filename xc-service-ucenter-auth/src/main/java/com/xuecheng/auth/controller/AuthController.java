package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/11- 10:01
 */
@RestController
//(contextPath = /auth)
public class AuthController implements AuthControllerApi {

    @Autowired
    AuthService authService;
    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        //申请令牌
        AuthToken authToken = authService.login(username,password,clientId,clientSecret);

        //返回cookie
        saveCookie(authToken);
        return new LoginResult(CommonCode.SUCCESS,authToken.getAccess_token());
    }

    private void saveCookie(AuthToken authToken) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",authToken.getAccess_token(),cookieMaxAge,false);
    }


    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout(@CookieValue(value = "uid",required = false) String accessToken) {
        authService.logout(accessToken);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     *  获得jwt长令牌
     * @return
     */
    @Override
    @GetMapping("/userjwt")
    public JwtResult userjwt(@CookieValue(value = "uid",required = false) String accessToken) {
        if(null == accessToken){
            return new JwtResult(CommonCode.FAIL,null);
        }
        //从redis中获得jwt长令牌
        String jwtToken = authService.getJwtFromRedisByAccessToken(accessToken);
        if(jwtToken != null){
            return new JwtResult(CommonCode.SUCCESS,jwtToken);
        }

        return new JwtResult(CommonCode.FAIL,null);
    }
}
