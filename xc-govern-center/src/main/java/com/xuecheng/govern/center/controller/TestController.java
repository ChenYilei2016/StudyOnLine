package com.xuecheng.govern.center.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/04- 18:21
 */
@Controller
public class TestController {

    @RequestMapping("/m")
    public String ss(){
        return "h";
    }
}
