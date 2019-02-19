package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/15- 12:15
 */
@Service
@Transactional
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    RestTemplate restTemplate;

    //文件mongo的操作类
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

    public QueryResponseResult findList(int page,int size, QueryPageRequest queryPageRequest) {
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        CmsPage cmsPage = new CmsPage();
        BeanUtils.copyProperties(queryPageRequest,cmsPage);

        if(page<=0){
            page = 1;
        }
        if(size<=0){
            size = 10;
        }
        page = page -1;
        //根据cmsPage的属性查 ,pageAliase模糊查找
        Page<CmsPage> pages = cmsPageRepository.findAll(Example
                .of(cmsPage, ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains()))
                ,PageRequest.of(page, size));

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,pages.getContent(),pages.getTotalElements());
        return queryResponseResult;
    }

    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage query = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if(null == query){
            CmsPage save = cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,save);
        }
        else{
            return new CmsPageResult(CommonCode.FAIL,null);
        }
    }

    //根据页面id查询页面
    public CmsPage getById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    //修改页面
    public CmsPageResult update(String id,CmsPage cmsPage){
        //根据id从数据库查询页面信息
        CmsPage one = this.getById(id);
        if(one!=null){
            //准备更新数据
            //设置要修改的数据
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            one.setDataUrl(cmsPage.getDataUrl());
            //提交修改
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS,one);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL,null);

    }


    //页面静态化 使用pageId: 5a795ac7dd573c04508f3a56
    //1. 得到Model 数据
    //2. 得到template
    //3. 生成 .html (这里直接返回字符串结果)
    public String getPageHtml(String pageId){
        Optional<CmsPage> oCmsPage = cmsPageRepository.findById(pageId);
        if(!oCmsPage.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOT_EXIST);
        }
        CmsPage cmsPage = oCmsPage.get();
        //1. 得到Model 数据
        String dataUrl = cmsPage.getDataUrl();
        Map model =new HashMap();
        model.put("model",restTemplate.getForObject(dataUrl, Map.class));
        System.out.println("model: "+model);
        if(null == model.get("model")){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //2. 得到template
        String templateId = cmsPage.getTemplateId();
        Optional<CmsTemplate> oCmsTemplate = cmsTemplateRepository.findById(templateId);
        if(!oCmsTemplate.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        CmsTemplate cmsTemplate = oCmsTemplate.get();
        String fileId = cmsTemplate.getTemplateFileId();
        String template = null;
        try {
            GridFSDownloadStream stream = gridFSBucket.openDownloadStream(new ObjectId(fileId));
            template = IOUtils.toString(stream, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        //3. 生成 .html (这里直接返回字符串结果)
        try {
            Template templateObject = new Template("template", template, new Configuration(Configuration.getVersion()));
            return FreeMarkerTemplateUtils.processTemplateIntoString(templateObject,model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

