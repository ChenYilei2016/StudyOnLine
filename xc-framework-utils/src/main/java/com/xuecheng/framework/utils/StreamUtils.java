package com.xuecheng.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/06- 15:38
 */
public class StreamUtils {

    public static void closeStream(InputStream inputStream){
        try {
            if(null != inputStream)
                inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("关闭 inputStream io流异常");
        }
    }
    public static void closeStream(OutputStream outputStream){
        try {
            if(null != outputStream)
                outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("关闭 outputStream io流异常");
        }
    }
}
