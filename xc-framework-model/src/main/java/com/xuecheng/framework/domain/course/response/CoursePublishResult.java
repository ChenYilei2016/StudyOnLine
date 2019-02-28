package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/28- 15:49
 */
@Data
public class CoursePublishResult extends ResponseResult {
    String previewUrl;
    public CoursePublishResult(String previewUrl, ResultCode resultCode){
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
