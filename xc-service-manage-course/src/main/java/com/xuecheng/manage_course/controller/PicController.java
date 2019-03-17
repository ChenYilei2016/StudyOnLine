package com.xuecheng.manage_course.controller;

import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.client.CmsPageClient;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/24- 14:08
 */
//自己添加的
@Controller
@Api("图片的下载")
public class PicController{
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    //简化操作代替nginx 实现图片的下载
    //单纯简化操作.. 实际上应该整合nginx和fastdfs,

//    http://localhost:31200/img?imgPath=group1/M00/00/00/rBsACFxyNqKAVz8EAAfvaiiDjBU796.bmp
    @GetMapping("/img") // group1/M00/00/00/rBsACFxyNqKAVz8EAAfvaiiDjBU796.bmp
    public void down(@RequestParam("imgPath") String imgPath, HttpServletResponse response, HttpServletRequest request){
        String group = StringUtils.substringBefore(imgPath, "/");//组别
        String path = StringUtils.substringAfter(imgPath, "/");//路径
        String after = StringUtils.substringAfter(imgPath, ".");//后缀
        if(StringUtils.isBlank(group)||StringUtils.isBlank(path)||StringUtils.isBlank(after)){
            ExceptionCast.cast(CommonCode.IMGPATH_ERROR);
        }
        fastFileStorageClient.downloadFile(group,path, new DownloadCallback<Object>() {
            @Override
            public Object recv(InputStream ins) throws IOException {
                response.setContentType("image/"+after);
                ServletOutputStream outputStream = response.getOutputStream();
                IOUtils.copy(ins,outputStream);
                outputStream.flush();
                outputStream.close();
                return null;
            }
        });
    }
    @Autowired
    CmsPageClient cmsPageClient;
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/testFeign")
    @ResponseBody
    public Object testFeign(){
        ResponseEntity<CategoryNode> forEntity = restTemplate.getForEntity("http://127.0.0.1:31200/course/category/list", CategoryNode.class);
        return forEntity.getBody();
    }
}
