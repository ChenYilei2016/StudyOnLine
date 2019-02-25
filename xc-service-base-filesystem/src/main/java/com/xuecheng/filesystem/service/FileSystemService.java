package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/25- 10:28
 */
@Service
@Transactional
public class FileSystemService {
    //存储文件
    @Autowired
    FastFileStorageClient fastFileStorageClient;
    //存储文件信息
    @Autowired
    FileSystemRepository fileSystemRepository;

    // group1/M00/00/00/rBsACFxzZ3KAK7bfAAAJnnZd-3Y238.ico
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata){
        //1 存入fastdfs
        InputStream stream = null;
        try {
            stream = multipartFile.getInputStream();
        } catch (IOException e) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        StorePath storePath = fastFileStorageClient.uploadFile(stream, size, StringUtils.substringAfterLast(originalFilename, "."), null);
        if(null == storePath){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //2 信息加入mongodb
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(storePath.getFullPath());
        fileSystem.setFilePath(storePath.getFullPath());
        fileSystem.setFileSize(size);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        fileSystem.setFileType(multipartFile.getContentType());
        try {
            if(StringUtils.isNotBlank(metadata)){
                fileSystem.setMetadata(JSONObject.parseObject(metadata, Map.class));
            }
        }catch (Exception e){}
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }
}
