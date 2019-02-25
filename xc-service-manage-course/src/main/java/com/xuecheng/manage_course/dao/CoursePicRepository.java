package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/24- 9:07
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    long deleteByCourseid(String courseId);
}
