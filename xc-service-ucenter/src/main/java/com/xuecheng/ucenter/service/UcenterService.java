package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.mysqldao.XcCompanyUserRepository;
import com.xuecheng.ucenter.mysqldao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/11- 17:43
 */

@Service
public class UcenterService {
    @Autowired
    XcUserRepository xcUserRepository;
    @Autowired
    XcCompanyUserRepository xcCompanyUserRepository;

    public XcUserExt getUserExt(String username){
        XcUserExt xcUserExt = new XcUserExt();
        XcUser xcUser = xcUserRepository.findByUsername(username);
        if(null == xcUser){
            return null;
        }
        XcCompanyUser companyUser = xcCompanyUserRepository.findByUserId(xcUser.getId());
        BeanUtils.copyProperties(xcUser,xcUserExt);
        if(companyUser != null){
            xcUserExt.setCompanyId(companyUser.getCompanyId());
        }
        //这个用户的权限列表

        return xcUserExt;
    }
}
