package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * xuecheng:
 *   elasticsearch:
 *     hostlist: ${eshostlist:127.0.0.1:9200} #多个结点中间用逗号分隔
 *
 * <dependency>
 *     <groupId>org.elasticsearch.client</groupId>
 *     <artifactId>elasticsearch-rest-high-level-client</artifactId>
 *     <version>6.2.1</version>
 * </dependency>
 **/
@Configuration
public class ElasticsearchConfig {

    @Value("${xuecheng.elasticsearch.hostlist}")
    private String hostlist;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        //解析hostlist配置信息
        String[] split = hostlist.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for(int i=0;i<split.length;i++){
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        //创建RestHighLevelClient客户端
        return new RestHighLevelClient(RestClient.builder(httpHostArray));
    }

    //项目主要使用RestHighLevelClient，对于低级的客户端暂时不用
    @Bean
    public RestClient restClient(){
        //解析hostlist配置信息
        String[] split = hostlist.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for(int i=0;i<split.length;i++){
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        return RestClient.builder(httpHostArray).build();
    }

}

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
//        sb.from(0);  sb.size(3); //7. 分页2条
//        HighlightBuilder hb = new HighlightBuilder();
//        hb.preTags("<tag>");hb.postTags("</tag>");
//        hb.field("name");
//        sb.highlighter(hb);//8 关键字 高亮显示 加些标签
////        sb.sort("属性", SortOrder.ASC); //9 排序!
//        searchRequest.source(sb);
//
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

