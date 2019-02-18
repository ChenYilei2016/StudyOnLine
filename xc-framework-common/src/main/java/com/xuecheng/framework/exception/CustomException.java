package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/17- 22:06
 */
@Data
public class CustomException extends RuntimeException {

    ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        super(resultCode.message());
        this.resultCode = resultCode;
    }
}
