package com.xuecheng.api.sys;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/02/23- 20:03
 */
@Api("字典查询")
public interface SysDictionaryControllerApi {
    @ApiOperation("根据dtype 查询属性字典")
    SysDictionary getDictionary(String dtype);
}
