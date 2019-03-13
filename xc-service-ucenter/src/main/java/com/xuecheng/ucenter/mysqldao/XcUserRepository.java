package com.xuecheng.ucenter.mysqldao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/11- 17:41
 */
public interface XcUserRepository extends JpaRepository<XcUser,String> {
    XcUser findByUsername(String username);
}
