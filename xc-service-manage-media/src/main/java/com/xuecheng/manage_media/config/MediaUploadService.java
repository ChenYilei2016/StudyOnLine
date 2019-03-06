package com.xuecheng.manage_media.config;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.MD5Util;
import com.xuecheng.framework.utils.PathUtil;
import com.xuecheng.framework.utils.StreamUtils;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/05- 20:09
 */
@Service
public class MediaUploadService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Value("${xc-service-manage-media.upload-location}")
    private String uploadPath ;

    /**
     *  进行文件的检查,是不是已经存在?
     */
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        String pathByMd5 = PathUtil.getPathByMd5(uploadPath, fileMd5);
        File targetFile = new File(pathByMd5+fileName);
        Optional<MediaFile> byId = mediaFileRepository.findById(fileMd5);
        //文件已经存在了,不用上传
        if(targetFile.exists() && byId.isPresent()){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //文件夹不存在则创建
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //得到块文件所属目录路径
    private String getChunkFileFolderPath(String fileMd5){
        return  uploadPath + fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/chunk/";
    }
    //得到文件的路径
    private String getFilePath(String fileMd5,String fileExt){
        return uploadPath + fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" + fileMd5 + "." +fileExt;
    }
    public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        String pathByMd5 = getChunkFileFolderPath(fileMd5);
        File chunkFile = new File(pathByMd5+ chunk);
        if(chunkFile.exists()){
            return new CheckChunkResult(CommonCode.SUCCESS,true);
        }else{
            return new CheckChunkResult(CommonCode.SUCCESS,false);
        }
    }


    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        if(null == file || file.getSize() == 0){
            ExceptionCast.cast(CommonCode.FAIL);
        }

        //创建文件夹
        if( !new File(getChunkFileFolderPath(fileMd5)).exists() ){
            new File(getChunkFileFolderPath(fileMd5)).mkdirs();
        }

        //上传分块
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = file.getInputStream();
            File targetFile = new File(getChunkFileFolderPath(fileMd5)+chunk);
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
            ExceptionCast.cast(CommonCode.FAIL);
        }finally {
            StreamUtils.closeStream(inputStream);
            StreamUtils.closeStream(fileOutputStream);
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 合并 分块
     * @return
     */
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) throws IOException{
        //将所有分块 ==> targetFile
        File targetFile = new File( this.getFilePath(fileMd5, fileExt) ); //合并文件
        FileOutputStream outputStream = new FileOutputStream(targetFile);
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        int i = 0;
        int count = 0;
        byte[] buf = new byte[1024];
        while(i < 99){
            File chunkFile = new File(chunkFileFolderPath+i);
            if(!chunkFile.exists()){
                //已无剩余合并文件
                break;
            }
            //合并数据
            FileInputStream inputStream =new FileInputStream(chunkFile);
            while( (count = inputStream.read(buf) ) != -1){
                outputStream.write(buf,0,count);
            }
            StreamUtils.closeStream(inputStream);
            i++;
        }
        StreamUtils.closeStream(outputStream);

        //校验 MD5
        String fileMD5String = MD5Util.getFileMD5String(targetFile);
        if(!fileMd5.equals( fileMD5String )){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        //写入mongodb文件信息
        //3、将文件的信息写入mongodb
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5 + "." +fileExt);
        //文件路径保存相对路径
        String filePath1 = fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" + fileMd5 + "." +fileExt;
        mediaFile.setFilePath(filePath1);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);

        return new ResponseResult(CommonCode.SUCCESS);
    }
}
