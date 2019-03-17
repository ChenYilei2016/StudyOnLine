package com.xuecheng.manage_course.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 *  fastdfs引入
 *
 *         <dependency>
 *             <groupId>com.github.tobato</groupId>
 *             <artifactId>fastdfs-client</artifactId>
 *             <version>1.26.2</version>
 *         </dependency>
 *
 * fdfs:
 *   connect-timeout: 2000
 *   tracker-list:
 *     - 132.232.117.84:22122
 *   so-timeout: 1500
 *
 * @author chenyilei
 * @date 2019/02/24- 14:37
 */

@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FdfsConfig {
    // 导入依赖组件
}