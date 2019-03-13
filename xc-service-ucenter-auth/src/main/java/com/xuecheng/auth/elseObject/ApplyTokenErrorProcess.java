package com.xuecheng.auth.elseObject;

import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;

import java.util.Map;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/12- 11:56
 */
public class ApplyTokenErrorProcess implements ErrorProcess {

    @Override
    public void process(Map map) {
        //解析spring security返回的错误信息
        if(map!=null && map.get("error_description")!=null){
            String error_description = (String) map.get("error_description");
            if(error_description.indexOf("UserDetailsService returned null")>=0){
                ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
            }else if(error_description.indexOf("坏的凭证")>=0){
                ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
            }
        }
    }
}
