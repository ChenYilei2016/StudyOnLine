package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/25- 10:28
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
