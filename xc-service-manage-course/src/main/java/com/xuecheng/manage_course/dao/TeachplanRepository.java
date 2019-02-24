package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/22- 15:24
 */

public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
}
