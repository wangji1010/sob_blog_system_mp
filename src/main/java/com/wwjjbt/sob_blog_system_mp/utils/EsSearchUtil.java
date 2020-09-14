package com.wwjjbt.sob_blog_system_mp.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbArticleMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbArticle;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EsSearchUtil {
    /*
     *添加文章到es索引库
     * */
    @Autowired
    private TbArticleMapper articleMapper;

    @Autowired
    private RestHighLevelClient client;

    public void addEsArticle(TbArticle article) throws IOException {
        //创建请求
        String id = article.getId();
        BulkRequest ceshi = new BulkRequest();
            ceshi.add(new IndexRequest("ceshi1")
                    .id(article.getId()).source(JSON.toJSONString(article), XContentType.JSON));
        client.bulk(ceshi, RequestOptions.DEFAULT);

    }

//    public static void updateEsArticle(String id,TbArticle article) throws IOException {
//        UpdateRequest updateRequest = new UpdateRequest("ceshi1", id);
//        updateRequest.doc(JSON.toJSONString(article),XContentType.JSON);
//        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
//        System.out.println(update.status());
//    }
//
//    public static void deleteEsArticle(String articleId) throws IOException {
//        DeleteRequest ceshi1 = new DeleteRequest("ceshi1", articleId);
//        DeleteResponse delete = client.delete(ceshi1, RequestOptions.DEFAULT);
//        System.out.println(delete.status());
//
//
//    }

}
