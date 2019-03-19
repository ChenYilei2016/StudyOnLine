package com.xuecheng.auth.service;

import com.xuecheng.auth.client.UserClient;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.utils.BCryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * spring security {/oauth/token 接口}
 * 返回相关信息,用于框架生成 整个令牌(包括 access jwt refresh等)
 * 判断账号密码是否正确
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

//    @Autowired
//    ClientDetailsService clientDetailsService;

    @Autowired
    UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //这里不是password模式
//        //取出身份，如果身份为空说明没有认证
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
//        if(authentication==null){
//            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
//            if(clientDetails!=null){
//                //密码
//                String clientSecret = clientDetails.getClientSecret();
//                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList(""));
//            }
//        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        /**
         *  真实信息查询
         *  根据传入的username 确认是否真有此用户
         * @return 返回null 无用户
         * @return 返回一个{@link {@link User}} 的子类,并会包装成token
         */
        XcUserExt userext = null;
        try {
            //远程调用获得用户信息
            userext = userClient.getUserext(username);
        }catch (Exception e){
            return null;
        }
        if(userext == null){
            return null;
        }
        //取出正确密码（hash值）
        String password = userext.getPassword();

        //用户权限，这里暂时使用静态数据，最终会从数据库读取
        //从数据库获取权限
        List<XcMenu> permissions = userext.getPermissions();
        userext.setPermissions(permissions);
        List<String> user_permission = new ArrayList<>();
        user_permission.add("course_get_baseinfo");
        user_permission.add("course_find_pic");
        permissions.forEach(item-> {
            user_permission.add(item.getCode());
        });
        //String user_permission_string  = StringUtils.join(user_permission.toArray(), ","); // 1,2,3 这种格式
        List<GrantedAuthority> myList = AuthorityUtils.createAuthorityList(user_permission.toArray(new String[user_permission.size()]));
        UserJwt userDetails = new UserJwt(username,
                password,
                myList);//AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string)
        userDetails.setId(userext.getId());
        userDetails.setUtype(userext.getUtype());//用户类型
        userDetails.setCompanyId(userext.getCompanyId());//所属企业
        userDetails.setName(userext.getName());//用户名称
        userDetails.setUserpic(userext.getUserpic());//用户头像

        //可以增加roles
//        User.withUserDetails(..).build()

        return userDetails;
    }
}
