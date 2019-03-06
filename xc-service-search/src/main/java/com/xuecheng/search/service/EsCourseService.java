package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/03- 16:02
 */
@Service
public class EsCourseService {
    @Autowired
    RestHighLevelClient client;
    @Value("${xuecheng.course.index}")
    private String index;
    @Value("${xuecheng.course.type}")
    private String type;
    @Value("${xuecheng.course.source_field}")
    private String source_field ;

    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) throws IOException {
        if(null == courseSearchParam){
            courseSearchParam = new CourseSearchParam();
        }

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sb= new SearchSourceBuilder(); BoolQueryBuilder sbb = QueryBuilders.boolQuery();   sb.query(sbb); //包装
        searchRequest.types(type);
        searchRequest.source(sb);
        sb.fetchSource(source_field.split(","),null);
        sb.from(page-1); sb.size(size);
        //根据关键字来匹配 keyword
        if(StringUtils.isNotBlank(courseSearchParam.getKeyword())){
            String keyword = courseSearchParam.getKeyword();
            sbb.must(QueryBuilders.multiMatchQuery(keyword,"name","teachplan","description").field("name",10));
        }
        //分类
        if(StringUtils.isNotBlank(courseSearchParam.getSt())){
            sbb.filter(QueryBuilders.matchQuery("st",courseSearchParam.getSt()));
        }
        if(StringUtils.isNotBlank(courseSearchParam.getMt())){
            sbb.filter(QueryBuilders.matchQuery("mt",courseSearchParam.getMt()));
        }
        //等级
        if(StringUtils.isNotBlank(courseSearchParam.getGrade())){
            sbb.filter(QueryBuilders.matchQuery("grade",courseSearchParam.getGrade()));
        }
        //高亮设置
        HighlightBuilder hb= new HighlightBuilder();
        hb = hb.field("name");
        hb.preTags("<tag>"); hb.postTags("</tag>");
        sb.highlighter(hb);

        //查询整理数据
        SearchResponse search = client.search(searchRequest);
        List<CoursePub> coursePubList = new ArrayList<>();
        for (SearchHit hit : search.getHits()) {
//            hit.getHighlightFields();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            CoursePub coursePub = new CoursePub();
            //高亮名字
            HighlightField name = hit.getHighlightFields().get("name");
            StringBuffer stringBuffer = new StringBuffer();
            for (Text fragment : name.getFragments()) {
                stringBuffer.append(fragment.string());
            }
            coursePub.setName(stringBuffer.toString());
            //图片
            String pic = (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            //价格
            if(null != sourceAsMap.get("price")){
                Float price =  Float.parseFloat(String.valueOf((Double) sourceAsMap.get("price"))); coursePub.setPrice(price); }
            if(null != sourceAsMap.get("price_old")){
                Float price_old = Float.parseFloat(String.valueOf((Double) sourceAsMap.get("price_old")));  coursePub.setPrice_old(price_old);}
            if(coursePub.getName() != null)
                coursePubList.add(coursePub);
        }
        return new QueryResponseResult<CoursePub>(CommonCode.SUCCESS,coursePubList,search.getHits().totalHits);
    }
}
