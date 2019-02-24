package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/17- 22:24
 */
@ControllerAdvice
@Slf4j
public class ExceptionCatch {

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public ResponseResult customCatch(CustomException cex){
        log.error("catch exception:{}",cex.getResultCode().message());
        return new ResponseResult(cex.getResultCode());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult allCatch(Exception e){
        e.printStackTrace();
        System.err.println(e.getMessage());
        //可采用map收集
        if(e instanceof HttpMessageNotReadableException){
            return new ResponseResult(CommonCode.INVALID_PARAM);
        }
        return new ResponseResult(false,99999,e.getMessage());
    }

}
