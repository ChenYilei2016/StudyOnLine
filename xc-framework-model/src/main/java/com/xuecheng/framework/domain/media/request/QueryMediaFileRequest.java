package com.xuecheng.framework.domain.media.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;

@Data
public class QueryMediaFileRequest extends RequestData {
    //查询名字
    private String fileOriginalName;
    //查询处理状态e;
    private String processStatus;
    //额外
    private String tag;
}
