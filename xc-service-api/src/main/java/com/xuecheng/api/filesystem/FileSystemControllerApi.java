package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/25- 10:17
 */
@Api(value = "文件系统")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件")
    public UploadFileResult uploadFile( MultipartFile multipartFile,
                                        String filetag,
                                        String businesskey,
                                        String metadata);

}
