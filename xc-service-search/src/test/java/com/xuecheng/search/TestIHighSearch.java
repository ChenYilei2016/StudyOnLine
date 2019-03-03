//package com.xuecheng.search;
//
//import cn.hutool.crypto.SecureUtil;
//import com.xuecheng.framework.utils.BCryptUtil;
//import io.jsonwebtoken.Jwts;
//import org.apache.catalina.security.SecurityUtil;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.text.Text;
//import org.elasticsearch.index.query.Operator;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
//import org.elasticsearch.search.sort.SortOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.util.EncodingUtils;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.management.Query;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * @author Administrator
// * @version 1.0
// **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class TestIHighSearch {
//
//    @Autowired
//    RestHighLevelClient client;
//
//    @Autowired
//    RestClient restClient;
//
////    @Autowired
////    ElasticsearchTemplate template;
////    NativeSearchQueryBuilder sqb= new NativeSearchQueryBuilder();
////        sqb.withQuery(QueryBuilders.matchAllQuery());
////        sqb.withPageable(PageRequest.of(1,1));
////        template.queryForPage(sqb.build(),null);
//
//    @Test
//    public void searchAll() throws IOException {
//        SearchRequest searchRequest = new SearchRequest("xc_course");
//        searchRequest.types("doc");
//
//        //真正的查询建造器
//        SearchSourceBuilder sb= new SearchSourceBuilder();
//        sb.query(QueryBuilders.matchAllQuery() );//1. 查所有
////        sb.query(QueryBuilders.termsQuery("name","spring")); //2, 搜索的词不拆分
//        sb.query(QueryBuilders.idsQuery().addIds("1").addIds("2"));// 3. 根据id查
//        sb.query(QueryBuilders.matchQuery("name","spring开发").operator(Operator.OR)); //4 搜索的词会拆分 有一个就匹配
////        sb.query(QueryBuilders.boolQuery().must( QueryBuilders.matchQuery(..) ).mustNot()) // 5 bool 可以组合不同的查询 !!!
////        QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("name","200002"))
////                                 .filter(QueryBuilders.rangeQuery("price").gt(60).lte(100));//6 过滤器基于bool 直接刷结果 不用算得分 较快
////        sb.query(QueryBuilders.multiMatchQuery("要查询的词","name","..."));    //10: 在多个字段匹配
//        sb.from(0);  sb.size(3); //7. 分页2条
//        HighlightBuilder hb = new HighlightBuilder();
//        hb.preTags("<tag>");hb.postTags("</tag>");
//        hb.field("name");
//        sb.highlighter(hb);//8 关键字 高亮显示 加些标签
////        sb.sort("属性", SortOrder.ASC); //9 排序!
//        searchRequest.source(sb);
//
//        //查询结果
//        SearchResponse response = client.search(searchRequest);
//        System.err.println("总体:"+response.toString()+"\n");
//        SearchHits hits = response.getHits();
//        for (SearchHit hit : hits.getHits()) {
//            System.out.println(hit.getSourceAsMap());
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            if(null != highlightFields){
//                HighlightField name = highlightFields.get("name");
//                Text[] fragments = name.getFragments();
//                fragments[0].toString();
//            }
//        }
//
//
//
//    }
//}
