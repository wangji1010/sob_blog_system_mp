package com.wwjjbt.sob_blog_system_mp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.wwjjbt.sob_blog_system_mp.mapper.TbArticleMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbCategoriesMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbSettingsMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbUserMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbArticle;
import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.pojo.TbSettings;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.*;
import io.jsonwebtoken.Claims;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSeedValueMDP;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import sun.applet.Main;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SobBlogSystemMpApplicationTests {

    @Resource
    private TbUserMapper userMapper;

    @Autowired
    private IdWorker idWorker;

    @Resource
    private TbSettingsMapper settingsMapper;

    @Resource
    private TbCategoriesMapper categoriesMapper;

    @Resource
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        TbUser user = new TbUser();
            user.setUserName("王吉");
            user.setId(idWorker.nextId()+"");
            user.setPassword("123456");
            user.setRoles("管理员");
            user.setEmail("212508940@qq.com");
            user.setAvatar("http://localhost:8888");
            user.setState("1");
            user.setRegIp("19.1245.15");
            user.setLoginIp("19.245.545");
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
        System.out.println(user);
        userMapper.insert(user);

    }

    @Test
    public void testSettings(){
        //更新设置表的，添加标记
        TbSettings settings = new TbSettings();
        settings.setId("10");
        settings.setKey("tag");
        settings.setValue("abda");
        settings.setCreateTime(new Date());
        settings.setUpdateTime(new Date());
        System.out.println("=============================>>>>>>settings==="+settings);
        settingsMapper.insertinto(settings);
    }

    @Test
    public void testMd5(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
//        $2a$10$irc2Zy6ahYG/c68ViZX7bOqfKzfPab6yUbi672MkxvLYcC3LJGRYG
//        $2a$10$OUQNI3vdKFjCvmS5CrxgJuzbfX8n2E5caZBkbj8fXLgdORr2xyCqi
        System.out.println(encode);
        String orgin = "123456";
        boolean ma = passwordEncoder.matches(orgin, "$2a$10$OUQNI3vdKFjCvmS5CrxgJuzbfX8n2E5caZBkbj8fXLgdORr2xyCqi");
        System.out.println(ma);
    }

    @Test
    public void testEmailSend(){
        SendMail.sendMail("212508940@qq.com","测试内容","今晚吃鸡！");
    }

    @Test
    public void testEmail(){
        boolean b = TextUtils.isEmailAddress("4879324@qq.com");
        System.out.println(b);

    }
    @Test
    public void testselectOne(){
        QueryWrapper<TbUser> emailWrapper = new QueryWrapper<>();
        emailWrapper.eq("email","2354879324@qq.com");
        System.out.println(userMapper.selectOne(emailWrapper));
    }

    @Test
    public void testToken(){
        TbUser user = new TbUser();
        user.setId("1111");
        user.setAvatar("http://9999");
        user.setUserName("root");
        user.setRoles("管理员");
        user.setEmail("2354879324@qq.com");
        user.setSign("wwewe");
        String token = JwtUtils.createJsonWebToken(user);
        System.out.println("========>>>>token::"+token);

    }

    /*
    * 解析token
    * */
    @Test
    public void testParseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmVoZWUiLCJpZCI6IjExMTEiLCJuYW1lIjoicm9vdCIsImF2YXRhciI6Imh0dHA6Ly85OTk5Iiwicm9sZSI6IueuoeeQhuWRmCIsImVtYWlsIjoiMjM1NDg3OTMyNEBxcS5jb20iLCJzaWduIjoid3dld2UiLCJpYXQiOjE1OTMyNDI4MTQsImV4cCI6MTU5MzI0MjkwMH0.0VGaNL56l8YdQhP_hzd40rDSyKSpwF5UpjHrvYZObFg";
        Claims claims = JwtUtils.checkJWT(token);



        System.out.println("==========>>>》claims:::"+claims);

    }

    @Test
    public void TestMd5(){
        String s = DigestUtils.md5DigestAsHex("admin_blog_-=".getBytes());
        System.out.println("MD5============>>>>>"+s);
    }
    @Test
    public void testJwtUtils(){
        TbUser user = new TbUser();
        user.setId("1001");
        user.setUserName("王吉");
        user.setAvatar("http://8080");
        String token = JwtTokenUtils.createToken(user, 1);
        System.out.println("======>>>>token:::"+token);
    }

    @Test
    public void testCheckUtils(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEwMDEiLCJhdmF0YXIiOiJodHRwOi8vODA4MCIsImV4cCI6MTU5Mzg0OTY0NywiaWF0IjoxNTkzMjQ0ODQ3LCJ1c2VybmFtZSI6IueOi-WQiSJ9.uFbW3wlb9JuVbSTNUuhOU7O6Si1BtS9uDs7A6o0JbaY";
        TbUser user = JwtTokenUtils.checkToken(token);
        System.out.println(user);
    }

    @Test
    public void testPageHelp(){
        PageHelper.startPage(2,2);
        List<TbUser> users = userMapper.selectList(null);
        System.out.println("Total: " + ((Page) users).getTotal());
        System.out.println("pages==>>"+((Page<TbUser>) users).getPages());
        System.out.println("pagenum==>>"+((Page<TbUser>) users).getPageNum());
        System.out.println("pagesize==>>"+((Page<TbUser>) users).getPageSize());
        for (TbUser user: users) {
            System.out.println(user);
        }
    }

    @Test
    public void testCategory(){
        TbCategories categories = new TbCategories();
        categories.setId("13456");
        categories.setcName("java");
        categories.setPinyin("pinyin");
        categories.setDescription("描述");
        categories.setcOrder(2);
        categories.setcStatus("1");
        categories.setCreateTime(new Date());
        categories.setUpdateTime(new Date());

        categoriesMapper.insert(categories);

    }

    @Test
   public void testEs() throws IOException {

        //创建请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("ceshi1");
        //执行请求
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);

    }

    @Test
    public void testExist(){

    }


    @Resource
    private TbArticleMapper articleMapper;
    @Test
    public void testAddEs() throws IOException {
//        TbUser user = userMapper.selectById("1275431122951471104");
        List<TbArticle> tbArticles = articleMapper.selectList(null);

        //创建请求
        BulkRequest ceshi = new BulkRequest();
        for (int i = 0; i < tbArticles.size(); i++) {
            System.out.println("========"+tbArticles.get(i));
            ceshi.add(new IndexRequest("ceshi1").id(tbArticles.get(i).getId()).source(JSON.toJSONString(tbArticles.get(i)),XContentType.JSON));
        }
        BulkResponse responses = client.bulk(ceshi, RequestOptions.DEFAULT);
        System.out.println(responses.status());
    }

    @Test
    public void testEsQuery() throws IOException {
            //创建请求
        SearchRequest searchRequest = new SearchRequest("ceshi1");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("content", "3测试文章加入es");
        searchSourceBuilder.query(termQueryBuilder);
//        searchSourceBuilder.from(1);
//        searchSourceBuilder.size(3);

        searchRequest.source(searchSourceBuilder);

        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println("====="+hit.getSourceAsMap());
            System.out.println("====="+hit.getSourceAsString());
        }
    }
    @Test
    public void testgetes() throws IOException {
        GetRequest ceshi = new GetRequest("ceshi", "1280390555733327872");
        GetResponse documentFields = client.get(ceshi, RequestOptions.DEFAULT);
        System.out.println(documentFields.getSourceAsString());
    }

    @Test
    public void testdelete() throws IOException {
        DeleteRequest ceshi1 = new DeleteRequest("ceshi1", "1280412877336870912");
        DeleteResponse delete = client.delete(ceshi1, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    @Test
    public void testMDtoHTML(){

        MdtoHtml mdtoHtml = new MdtoHtml();
        mdtoHtml.md_to_ht("");
                /*String md = "";
                MutableDataSet options = new MutableDataSet();

                Parser parser = Parser.builder(options).build();
                HtmlRenderer renderer = HtmlRenderer.builder(options).build();

                // You can re-use parser and renderer instances
                Node document = parser.parse(md);
                String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
                System.out.println(html);*/


    }

}
