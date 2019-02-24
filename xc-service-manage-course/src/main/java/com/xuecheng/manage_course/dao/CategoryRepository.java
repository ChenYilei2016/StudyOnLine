package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/23- 16:57
 */
public interface CategoryRepository extends JpaRepository<Category, String> {
}
