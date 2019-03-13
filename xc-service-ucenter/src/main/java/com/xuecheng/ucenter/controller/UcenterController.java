package com.xuecheng.ucenter.controller;

import com.xuecheng.api.ucenter.UcenterControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UcenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/11- 17:29
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {
    @Autowired
    UcenterService ucenterService;

    @Override
    @GetMapping("/getuserext")
    public XcUserExt getUserext(String username) {
        return ucenterService.getUserExt(username);
    }
}
