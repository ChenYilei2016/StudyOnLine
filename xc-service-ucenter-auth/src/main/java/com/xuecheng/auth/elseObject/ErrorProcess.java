package com.xuecheng.auth.elseObject;

import java.util.Map;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/12- 11:55
 */
public interface ErrorProcess {
    default void process(Map map){
        return ;
    }
}
