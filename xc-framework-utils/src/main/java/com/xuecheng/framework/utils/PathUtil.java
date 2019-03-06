package com.xuecheng.framework.utils;

import org.apache.commons.lang3.StringUtils;
import sun.security.rsa.RSASignature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * --对路径的相关处理--
 *
 * @author chenyilei
 * @date 2019/03/05- 20:26
 */
public class PathUtil {
    /**
     * 根据md5 得到一个文件夹路径
     *  一级路径: md5码的 第一位
     *  二级路径: md5码的 第二位
     *  三级路径: md5码本身
     * @param basePath 基础路径
     * @param md5 md5值
     * @return 得到的路径
     */
    public static String getPathByMd5(String basePath, String md5){
        basePath = basePath.endsWith("/")?basePath:basePath+"/";
        return basePath+md5.substring(0,1)+"/"+md5.substring(1,2)+"/"+md5+"/";
    }

}
