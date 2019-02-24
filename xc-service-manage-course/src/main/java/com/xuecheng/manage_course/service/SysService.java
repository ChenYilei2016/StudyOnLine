package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.mongoDao.SysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/23- 16:16
 */
@Service
public class SysService {
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    SysRepository sysRepository;

    public SysDictionary getDictionary(String dtype) {
        SysDictionary sysDictionary = new SysDictionary();
//        sysDictionary.setDType(dtype);
//        return sysRepository.findAll(Example.of(sysDictionary));
        return sysRepository.findFirstByDType(dtype);
    }
}
