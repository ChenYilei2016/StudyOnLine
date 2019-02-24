package com.xuecheng.manage_course.mongoDao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/23- 20:11
 */
public interface SysRepository extends MongoRepository<SysDictionary,String>{
    SysDictionary findFirstByDType(String dtype);
}
