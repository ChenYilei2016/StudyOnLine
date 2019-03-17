package com.xuecheng.ucenter.mysqldao;

import com.xuecheng.framework.domain.ucenter.XcMenu;

import java.util.List;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/15- 20:57
 */
public interface PermissionMapper {
    //根据 userid 来得到所有权限
    List<XcMenu> findXcMenuListByUserId(String userId);
}
