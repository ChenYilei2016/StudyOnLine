package com.xuecheng.manage_course.controller;

import com.xuecheng.api.sys.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/21- 16:25
 */
@RestController
@RequestMapping("/sys")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    SysService sysService;

    @Override
    @GetMapping("/dictionary/get/{dtype}")
    public SysDictionary getDictionary(@PathVariable("dtype") String dtype) {
        return sysService.getDictionary(dtype);
    }
}
