package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/20- 21:54
 */
@Slf4j
@Service
public class PageService {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    public void savePageToLocal(String pageId){
        CmsPage cmsPage = cmsPageRepository.findById(pageId).get();
        if(null == cmsPage){
            log.error("cmsPageRepository.findById 结果为null");
        }
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = cmsSiteRepository.findById(siteId).get();
        if(null == cmsSite){
            log.error("cmsSiteRepository.findById 结果为null");
        }

        String htmlFileId = cmsPage.getHtmlFileId();
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(new ObjectId(htmlFileId));
        String localPath = cmsSite.getSitePhysicalPath()+cmsPage.getPagePhysicalPath()+cmsPage.getPageName();

        FileOutputStream fileOutputStream =null;
        try {
            fileOutputStream = new FileOutputStream(localPath);
            IOUtils.copy(stream,fileOutputStream);
        } catch (IOException e) {
            log.error(" IOUtils.copy(stream,new FileOutputStream(localPath));");
            e.printStackTrace();
        }finally {
            if(null != fileOutputStream){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            stream.close();
        }
    }

}
