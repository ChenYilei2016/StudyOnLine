package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/15- 11:08
 */

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId,String pageName ,String pageWebPath);
}
