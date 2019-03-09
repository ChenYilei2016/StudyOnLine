package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/09- 13:54
 */
@Data
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {
    private String fileUrl ;
    public GetMediaResult(ResultCode resultCode , String url){
        super(resultCode);
        fileUrl = url;
    }
}
