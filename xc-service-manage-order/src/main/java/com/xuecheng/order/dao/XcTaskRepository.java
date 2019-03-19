package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/18- 21:01
 */
public interface XcTaskRepository extends JpaRepository<XcTask,String> {

    Page<XcTask> findByUpdateTimeBefore(Date date, Pageable pageable);

    //更新updateTime
    @Modifying
    @Query("update XcTask t set t.updateTime = :updateTime where t.id = :id")
    public int updateTaskTime(@Param(value = "id") String id, @Param(value = "updateTime") Date updateTime);

    @Modifying
    @Query("update XcTask t set t.version = :version+1 where t.id = :id and t.version = :version")
    public int updateTaskVersion(@Param(value = "id") String id,@Param(value = "version") int version);
}
