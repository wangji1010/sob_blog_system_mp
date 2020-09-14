package com.wwjjbt.sob_blog_system_mp.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

    /*
     * 1、导依赖
     * 2、找对象
     * 3、放到spring容器
     * 3、如果是springboot看源码
     * */
    @Value("${es.connection.host}")
    public String url;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(url,9200,"http")));
        return client;

    }
}
