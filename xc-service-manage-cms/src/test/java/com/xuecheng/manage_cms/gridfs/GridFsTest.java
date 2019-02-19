package com.xuecheng.manage_cms.gridfs;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/19- 11:28
 */
@SpringBootTest(value = "com.xuecheng.manage_cms")
@RunWith(SpringRunner.class)
public class GridFsTest {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
//    @Autowired
//    Template template;

    public static final String BASEURL= System.getProperty("user.dir")+"/src/test/resources/templates/";
    public static final String fileName="index_banner.ftl";

    @Test
    public void test1() throws IOException {
        File file =new File(BASEURL+fileName);
        //存文件 得到存的ID
//        ObjectId store = gridFsTemplate.store(new FileInputStream(file), fileName);
//        System.out.println(store); // 新:5c6b8f777faf443d34727fa5

        //取文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5c6b8f777faf443d34727fa5")));
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        System.out.println(IOUtils.toString(stream,"utf-8"));
        //有局限性 只能根据名字来取
//        System.out.println(gridFsTemplate.getResource(gridFSFile.getFilename()));
    }
}
