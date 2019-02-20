package com.xuecheng.manage_cms_client.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/19- 12:53
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    public String databaseName ;

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient){
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }
}
