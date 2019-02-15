package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class QueryResponseResult extends ResponseResult {

    QueryResult queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
        this.queryResult = queryResult;
    }

    public QueryResponseResult(ResultCode resultCode,List list,long total){
        super(resultCode);
        this.queryResult = new QueryResult(list,total);
    }

}
