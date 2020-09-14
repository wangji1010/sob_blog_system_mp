package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.SearchService;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TestEsServiceImpl implements SearchService {

    @Resource
    private  RestHighLevelClient client;
    private MatchQueryBuilder title;


    //2、从索引库获取数据实现搜索功能
    public ResponseResult search(String keywords,int pageNo,int pageSize,String categoryId,String sort) throws IOException {
        if (pageNo <= 1) {
            pageNo = 1;
        }


        CountRequest countRequest = new CountRequest("ceshi1");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("content", keywords));
        countRequest.source(searchSourceBuilder);

        //条件搜索
        SearchRequest goods = new SearchRequest("ceshi1");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //分页
        builder.from(pageNo);
        builder.size(pageSize);

        /*
         * 当sort 是1 的时候使用时间字段进行排序 2升序
         * 为3 时 使用 浏览量字段进行desc
         * 为4 时 使用 浏览量字段进行asc
         * */
        //根据字段排序
        if (!TextUtils.isEmpty(sort)) {
            if (sort.equals("1")) {
                builder.sort("createTime", SortOrder.DESC);
            }
            if (sort.equals("2")) {
                builder.sort("createTime", SortOrder.ASC);
            }
            if (sort.equals("3")) {
                builder.sort("viewCount", SortOrder.DESC);
            }
            if (sort.equals("4")) {
                builder.sort("viewCount", SortOrder.ASC);
            }
        }

        /*
         * 当 分类 为空的时候使用内容进行检索，否则使用 分类进行检索
         * */
        if (TextUtils.isEmpty(categoryId)) {
            title = QueryBuilders.matchQuery("content", keywords);
            builder.query(title);
            builder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("content");//高亮的字段
            highlightBuilder.requireFieldMatch();//是否多个字段都高亮
            highlightBuilder.preTags("<span style='color:red'>");//前缀后缀
            highlightBuilder.postTags("</span>");
            builder.highlighter(highlightBuilder);

            //执行搜索
            goods.source(builder);
            //总数
            CountResponse count = client.count(countRequest, RequestOptions.DEFAULT);
            long sumCount = count.getCount();
            //查询得结果
            SearchResponse search = client.search(goods, RequestOptions.DEFAULT);
            //解析结果
            ArrayList<Map<String,Object>> list = new ArrayList<>();
            Map<String, Object> pageNavigationMap = new HashMap<>();
            pageNavigationMap.put("currentPage",pageNo);
            pageNavigationMap.put("totalSize",sumCount-1);
            pageNavigationMap.put("totalPage",(int)Math.ceil(sumCount/pageSize));
            pageNavigationMap.put("pageSize",pageSize);

            for (SearchHit hit : search.getHits().getHits()) {
                //解析高亮的字段
                //获取高亮字段
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                System.out.println("=========="+highlightFields);
                HighlightField content = highlightFields.get("content");
                System.out.println("==content=="+content);
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果
                //将原来的字段替换为高亮字段即可
                if (content!=null){
                    Text[] fragments = content.fragments();
                    String newTitle = "";
                    for (Text text : fragments) {
                        newTitle +=text;
                    }
                    sourceAsMap.put("content",newTitle);//替换掉原来的内容
                }
                list.add(sourceAsMap);
            }
            list.add(0,pageNavigationMap);
            return ResponseResult.success("搜索成功").setData(list);
        } else {
            title = QueryBuilders.matchQuery("categoryId", categoryId);
            builder.query(title);
            builder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            SearchRequest request = goods.source(builder);
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            ArrayList<Map<String,Object>> list = new ArrayList<>();
            for (SearchHit hit : search.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果
                list.add(sourceAsMap);
            }
            log.info(list+"");
            return ResponseResult.success("根据分类获取成功！").setData(list);

        }


//        TermQueryBuilder title = QueryBuilders.termQuery("content",keywords);
//        QueryBuilders.matchPhraseQuery()


    }}




