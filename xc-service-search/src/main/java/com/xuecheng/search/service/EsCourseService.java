package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Value("${xuecheng.media.index}")
    private String media_index;
    @Value("${xuecheng.course.type}")
    private String type;
    @Value("${xuecheng.media.type}")
    private String media_type;
    @Value("${xuecheng.course.source_field}")
    private String source_field;
    @Value("${xuecheng.media.source_field}")
    private String media_source_field;

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

    public Map<String, CoursePub> getall(String id) {
        //定义一个搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //指定type
        searchRequest.types(type);

        //定义SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置使用termQuery
        searchSourceBuilder.query(QueryBuilders.termQuery("id",id));
        //过虑源字段，不用设置源字段，取出所有字段
//        searchSourceBuilder.fetchSource()
        searchRequest.source(searchSourceBuilder);
        //最终要返回的课程信息

        Map<String,CoursePub> map = new HashMap<>();
        try {
            SearchResponse search = client.search(searchRequest);
            SearchHits hits = search.getHits();
            SearchHit[] searchHits = hits.getHits();
            for(SearchHit hit:searchHits){
                CoursePub coursePub = new CoursePub();
                //获取源文档的内容
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //课程id
                String courseId = (String) sourceAsMap.get("id");
                String name = (String) sourceAsMap.get("name");
                String grade = (String) sourceAsMap.get("grade");
                String charge = (String) sourceAsMap.get("charge");
                String pic = (String) sourceAsMap.get("pic");
                String description = (String) sourceAsMap.get("description");
                String teachplan = (String) sourceAsMap.get("teachplan");
                coursePub.setId(courseId);
                coursePub.setName(name);
                coursePub.setPic(pic);
                coursePub.setGrade(grade);
                coursePub.setTeachplan(teachplan);
                coursePub.setDescription(description);
                map.put(courseId,coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return map;
    }

    //根据多个课程计划查询课程媒资信息
    public QueryResponseResult<TeachplanMediaPub> getmedia(String[] teachplanIds) {
        //定义一个搜索请求对象
        SearchRequest searchRequest = new SearchRequest(media_index);
        //指定type
        searchRequest.types(media_type);

        //定义SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置使用termsQuery根据多个id 查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id",teachplanIds));
        //过虑源字段
        String[] includes = media_source_field.split(",");
        searchSourceBuilder.fetchSource(includes,new String[]{});
        searchRequest.source(searchSourceBuilder);
        //使用es客户端进行搜索请求Es
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        long total = 0;
        try {
            //执行搜索
            SearchResponse search = client.search(searchRequest);
            SearchHits hits = search.getHits();
            total = hits.totalHits;
            SearchHit[] searchHits = hits.getHits();
            for(SearchHit hit:searchHits){
                TeachplanMediaPub teachplanMediaPub= new TeachplanMediaPub();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出课程计划媒资信息
                String courseid = (String) sourceAsMap.get("courseid");
                String media_id = (String) sourceAsMap.get("media_id");
                String media_url = (String) sourceAsMap.get("media_url");
                String teachplan_id = (String) sourceAsMap.get("teachplan_id");
                String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

                teachplanMediaPub.setCourseId(courseid);
                teachplanMediaPub.setMediaUrl(media_url);
                teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
                teachplanMediaPub.setMediaId(media_id);
                teachplanMediaPub.setTeachplanId(teachplan_id);
                teachplanMediaPubList.add(teachplanMediaPub);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //数据集合
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        queryResult.setTotal(total);
        QueryResponseResult<TeachplanMediaPub> queryResponseResult = new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
