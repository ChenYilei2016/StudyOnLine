package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/28- 19:48
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
