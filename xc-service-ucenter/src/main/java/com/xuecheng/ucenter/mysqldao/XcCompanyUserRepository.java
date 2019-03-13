package com.xuecheng.ucenter.mysqldao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/11- 17:42
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {
    XcCompanyUser findByUserId(String userId);
}
