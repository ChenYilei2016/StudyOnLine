package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/18- 19:05
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
