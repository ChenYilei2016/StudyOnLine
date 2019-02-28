package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/27- 19:17
 */
@Data
@NoArgsConstructor
public class CourseView implements Serializable {
    private CourseBase courseBase;
    private CourseMarket courseMarket;
    private CoursePic coursePic;
    private TeachplanNode teachplanNode;
}
