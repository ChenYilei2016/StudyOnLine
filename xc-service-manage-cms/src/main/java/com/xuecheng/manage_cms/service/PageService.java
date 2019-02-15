package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/15- 12:15
 */
@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    public QueryResponseResult findList(int page,int size, QueryPageRequest queryPageRequest) {

        if(page<=0){
            page = 1;
        }
        if(size<=0){
            size = 10;
        }
        page = page -1;
        Page<CmsPage> pages = cmsPageRepository.findAll(PageRequest.of(page, size));
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,pages.getContent(),pages.getTotalElements());
        return queryResponseResult;
    }
}
