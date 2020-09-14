package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.mapper.TbArticleMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLabelsMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbArticle;
import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLabels;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbArticleService;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.*;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Slf4j
@Service
public class TbArticleServiceImpl implements TbArticleService {

    @Autowired
    private TbUserService userService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbArticleMapper articleMapper;

    @Resource
    private TbLabelsMapper labelsMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RestHighLevelClient client;


    /*
    * 如果是多人博客系统，可以考虑审核问题
    * 保存成草稿：
    *       1、  用户手动提交
    *       2、代码自动提交
    * 不管是何种草稿-->必须有标题
    * 方案一：
    *       用户发文章之前，咸香后台请求一个唯一id
    *       如果是更新文章不需要请求唯一id，避免重复提交
    * 方案二：
    *       直接提交，后台判断数据库是否存在id，有则修改文章内容
    *        没有id则新创建，并且id作为此次返回结果
    * <p>
    防止重复提交（网络卡顿用户点击多次）
    通过id的方式
    * 通过token的提交频率来计算，如果30s内有多次提交只有最前的一次
    * 其他的方式直接return，提示
    * 前端的处理，点击提交之后  禁止按钮使用
    * </p>
    *
    * 自动保存草稿，保存在本地
    *
    * 手动提交，则提交到后台
    *
    * */
    @Override
    public ResponseResult addArticle(HttpServletRequest request, HttpServletResponse response, TbArticle article) throws IOException {


        //检查用户，获取用户对象
        TbUser user = userService.checkUser(request, response);
        //检查数据
        // title   类型
        String title = article.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.failed("标题不能为空");
        }
        String type = article.getType();
        if (TextUtils.isEmpty(type)) {
            return ResponseResult.failed("类型不能为空");
        }
        if (!type.equals("0") && !type.equals("1")) {
            return ResponseResult.failed("类型错误");
        }

        //发布文章的2种状态，草稿  和  发布

        String state = article.getState();
        if (!Constrants.Article.STATE_PULISH.equals(state) && !Constrants.Article.STATE_DRAFT.equals(state)) {
            //不支持该操作
            return ResponseResult.failed("不支持该操作");
        }

        //发布状态做检查，关键数据不能为空，  草稿状态则不需要检查数据，允许先为空
        if (Constrants.Article.STATE_PULISH.equals(state)) {
            if (title.length() > Constrants.Article.TITLE_LENGTH_MAX) {
                return ResponseResult.failed("标题长度不可以超过" + Constrants.Article.TITLE_LENGTH_MAX + "个字符");
            }

            String summary = article.getSummary();
            if (TextUtils.isEmpty(summary)) {
                return ResponseResult.failed("摘要不能为空");
            }
            if (summary.length() > Constrants.Article.SUMMARY_MAX_LENGTH) {
                return ResponseResult.failed("摘要不得超过" + Constrants.Article.SUMMARY_MAX_LENGTH + "个字符");
            }
            String content = article.getContent();
            if (TextUtils.isEmpty(content)) {
                return ResponseResult.failed("内容不能为空");
            }
            String labels = article.getLabels();

            if (TextUtils.isEmpty(labels)) {
                return ResponseResult.failed("标签不能为空");
            }
        }
        String articleId = article.getId();
        //不携带id则是发布  否则  是草稿
        if (TextUtils.isEmpty(articleId)) {
            //新的内容，数据库没有的
            //补充数据
            //id  创建时间  用户id  更新 创建时间
            article.setId(idWorker.nextId() + "");
            article.setCreateTime(new Date());

            article.setUserName(user.getUserName());
            article.setUserAvatar(user.getAvatar());

        } else {
            //更新内容,如果已经是发布的或者已经是草稿了，则不能再为草稿
            TbArticle articleFromDb = articleMapper.selectById(articleId);
            if (Constrants.Article.STATE_PULISH.equals(articleFromDb.getState()) &&
                    Constrants.Article.STATE_DRAFT.equals(state)) {
                //已经发布只能更新，不能保存草稿
                return ResponseResult.failed("已发布文章不能保存为草稿");
            }
        }
        article.setUpdateTime(new Date());
        article.setUserId(user.getId());
        //标签入库
        this.setupLabels(article.getLabels());
//        log.info("=========state+++:"+article.getState());
        //只有发布的文章才添加到es
        if (Constrants.Article.STATE_PULISH.equals(article.getState())){
            //将发布的文章同时发布到es
            BulkRequest ceshi = new BulkRequest();
            ceshi.add(new IndexRequest("ceshi1")
                    .id(article.getId()).source(JSON.toJSONString(article), XContentType.JSON));
            client.bulk(ceshi, RequestOptions.DEFAULT);
        }

        //        保存
        articleMapper.insert(article);

        //返回结果
        //如果要做程序自动保存草稿（如没30s保存一次，需要加上该id，否则会多次创建）
        return ResponseResult.success(Constrants.Article.STATE_DRAFT.equals(state) ? "草稿发布成功" : "文章发表成功").setData(article.getId());
    }
    private void setupLabels(String labels) {
        //将标签分割开加入集合
        ArrayList<String> labelList = new ArrayList<>();
        if (!labels.contains("-")) {
            labelList.add(labels);
        } else {
            labelList.addAll(Arrays.asList(labels.split("-")));
        }

        //入库,统计
        for (String label : labelList) {
          //根据集合中的标签逐一给数据库对应的记录数量加一，如果更新失败则创建该标签
            int result = labelsMapper.updateCountByName(label);
            log.info("result:::"+result);
            if (result==0){
                TbLabels labelByName = new TbLabels();
                labelByName.setId(idWorker.nextId() + "")
                        .setCount(1)
                        .setName(label)
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date());
                labelsMapper.insert(labelByName);
            }
        }

    }



    /*
     * 根据条件进行查询列表操作
     * 课根据状态（发布、草稿）、标题关键字、分类id查询
     * */
    @Override
    public ResponseResult getListArticle(int page, int size, String keywords, String categoryId, String state) {
        //处理一下 size  和  page
        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);

        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        TbUser user = userService.checkUser(request, response);


        //创建分页查询
        Page<TbArticle> page1 = new Page<>(checkPage, checkSize);
        QueryWrapper<TbArticle> wrapper = new QueryWrapper<>();
        //处理其他可选参数
        if (!TextUtils.isEmpty(keywords)) {
            wrapper.like("title", keywords);
        }
        if (!TextUtils.isEmpty(categoryId)) {

            //如果用户未登录或者无权限就只能获取到已发布，置顶的文章
            if (user == null || Constrants.User.ROLE_NORMAL.equals(user.getRoles())) {
                wrapper.eq("category_id", categoryId);
                wrapper.eq("state", Constrants.Article.STATE_PULISH);
                wrapper.or();
                wrapper.eq("state", Constrants.Article.STATE_TOP);
            } else if (Constrants.User.ROLE_ADMIN.equals(user.getRoles())) {
                wrapper.eq("category_id", categoryId);
            }


        }
        if (!TextUtils.isEmpty(state)) {
            wrapper.eq("state", state);
        }
        wrapper.orderByDesc("create_time");

        IPage<TbArticle> tbArticleIPage = articleMapper.selectPage(page1, wrapper);
        //处理查询条件
        //返回
        return ResponseResult.success("获取列表成功").setData(tbArticleIPage);
    }

    /*
    如果有审核机制只有管理员和作者可以看
    草稿 发布  置顶  已发布
    删除的不能获取，其他的可以获取
    * 获取文章单挑记录
    * */
    @Override
    public ResponseResult getArticleById(String articleId) {
        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        //查询出文章
        TbArticle tbArticle = articleMapper.selectById(articleId);
        if (tbArticle == null) {
            return ResponseResult.failed("文章不存在");
        }

        //判断文章的状态,
        /*
         * 普通用户可以获取自己的草稿，和已发布、置顶的文章
         * */
        String state = tbArticle.getState();
        if (Constrants.Article.STATE_PULISH.equals(state) ||
                Constrants.Article.STATE_TOP.equals(state) ||
                Constrants.Article.STATE_DRAFT.equals(state)) {
            return ResponseResult.success("获取文章成功").setData(tbArticle);
        }

        //如果是删除/草稿  需要管理角色
        TbUser user = userService.checkUser(request, response);
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN("您的访问有风险，请先登录验证身份");
        }
        String roles = user.getRoles();
        //未登录或者不是管理账户则不能获取
        if (!Constrants.User.ROLE_ADMIN.equals(roles)) {
            return ResponseResult.PERMISSION_FORBID("无权限访问");
        }
        //返回
        return ResponseResult.success("文章获取成功").setData(tbArticle);
    }

    /*
    该接口只支持修改
    标题、内容 、 标签、分类、摘要
    * 修改文章
    * */
    @Override
    public ResponseResult updateArticleById(String articleId, TbArticle article) throws IOException {
        //查询出数据
        TbArticle articleFromDb = articleMapper.selectById(articleId);
        if (articleFromDb == null) {
            return ResponseResult.failed("不存在该文章");
        }
        //内容修改
        String title = article.getTitle();
        if (!TextUtils.isEmpty(title)) {
            articleFromDb.setTitle(title);
        }
        String summary = article.getSummary();
        if (!TextUtils.isEmpty(summary)) {
            articleFromDb.setSummary(summary);
        }
        String labels = article.getLabels();
        if (!TextUtils.isEmpty(labels)) {
            articleFromDb.setLabels(labels);
        }
        String content = article.getContent();
        if (!TextUtils.isEmpty(content)) {
            articleFromDb.setContent(content);
        }
        String categoryId = article.getCategoryId();
        if (!TextUtils.isEmpty(categoryId)) {
            articleFromDb.setCategoryId(categoryId);
        }
        articleFromDb.setUpdateTime(new Date());
        //更新到数据库
        articleMapper.updateById(articleFromDb);
        UpdateRequest updateRequest = new UpdateRequest("ceshi1", articleId);
        updateRequest.doc(JSON.toJSONString(articleFromDb),XContentType.JSON);
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(update.status());
        //返回结果
        return ResponseResult.success("更新文章成功");
    }

    /*
     * 真正意义的删除
     * */
    @Override
    public ResponseResult deleteById(String articleId) throws IOException {
        int i = articleMapper.deleteById(articleId);
        if (i == 0) {
            return ResponseResult.failed("该文章不存在");
        }
//EsSearchUtil.deleteEsArticle(articleId);
        DeleteRequest ceshi1 = new DeleteRequest("ceshi1", articleId);
        DeleteResponse delete = client.delete(ceshi1, RequestOptions.DEFAULT);
        System.out.println(delete.status());
        return ResponseResult.success("删除成功");
    }

    /**
     * 修改文章状态删除
     */
    @Override
    public ResponseResult deleteByState(String articleId) {
        int i = articleMapper.deleteArticleByState(articleId);
        if (i == 0) {
            return ResponseResult.failed("该文章不存在");
        }
        return ResponseResult.success("删除成功");
    }

    /*
     * 将问章的状态进行置顶,修改state值
     * 只有已经发布的进行置顶，如果已经置顶了则取消置顶
     * */
    @Override
    public ResponseResult topArticle(String articleId) {
        TbArticle tbArticle = articleMapper.selectById(articleId);
        if (tbArticle == null) {
            return ResponseResult.failed("文章不存在");
        }
        String state = tbArticle.getState();
        if (Constrants.Article.STATE_PULISH.equals(state)) {
            tbArticle.setState(Constrants.Article.STATE_TOP);
            articleMapper.updateTopById(articleId);
            return ResponseResult.success("置顶成功");
        }
        if (Constrants.Article.STATE_TOP.equals(state)) {
            tbArticle.setState(Constrants.Article.STATE_PULISH);
            articleMapper.updateNotTopById(articleId);
            return ResponseResult.success("取消置顶成功");
        }
        return ResponseResult.failed("不支持该操作");

    }

    /*
     * 获取置顶的文章列表
     * 只获取state=3的文章
     * */
    @Override
    public ResponseResult getTopArticle() {
        QueryWrapper<TbArticle> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.eq("state", Constrants.Article.STATE_TOP);
        List<TbArticle> tbArticles = articleMapper.selectList(wrapper);

        return ResponseResult.success("查询置顶文章成功").setData(tbArticles);
    }

    /*
     * 获取推荐的列表
     * 通过标签来计算
     * */
    @Override
    public ResponseResult listRecommendArticleId(String articleId, int size) {
        //查询文章,只需要里面的标签
        String label = articleMapper.listArticlelabelById(articleId);
        //打散标签
        List<String> labels = new ArrayList<>();
        if (!label.contains("-")) {
            labels.add(label);
        } else {
            labels.addAll(Arrays.asList(label.split("-")));
        }
        log.info("========label" + labels);

        //从列表中随机获取一个标签，查询相似的文
        Random random = new Random();
        String targetLabel = labels.get(random.nextInt(labels.size()));
        log.info("==========targetlabel+++" + targetLabel);
        QueryWrapper<TbArticle> wrapper = new QueryWrapper<>();
        wrapper.like("labels", targetLabel);
        wrapper.eq("state", 1);
        wrapper.or();
        wrapper.eq("state", 3);
        wrapper.ne("id", articleId);
        wrapper.last("limit " + size);

        List<TbArticle> likeResultList = articleMapper.selectList(wrapper);
        log.info("likeResultList：：：" + likeResultList);

        //判断长度
        if (likeResultList.size() < size) {
            //说明不够数量，获取最新的作为补充
            int dxSize = size - likeResultList.size();
            QueryWrapper<TbArticle> wrapper1 = new QueryWrapper<>();
            wrapper1.ne("id", articleId).
                    eq("state", 1).
                    or().eq("state", 3).
                    last("limit " + dxSize).
                    orderByDesc("create_time");

            List<TbArticle> dxList = articleMapper.selectList(wrapper1);
            //把补充数据添加到之前的集合后面(可能导致文章出现重复)
            likeResultList.addAll(dxList);
        }
        return ResponseResult.success("获取推荐文章成功").setData(likeResultList);
    }



    /*
     * 根据标签获取文章列表，（分页）
     * */
    @Override
    public ResponseResult listArticles(int pages, int size, String label) {
        int checkPage = CheckPageSize.checkPage(pages);
        int checkSize = CheckPageSize.checkSize(size);

        Page<TbArticle> page = new Page<>(checkPage, checkSize);
        QueryWrapper<TbArticle> wrapper = new QueryWrapper<>();
        wrapper.like("labels", label)
                .ne("state", Constrants.Article.STATE_DELETE)
                .ne("state", Constrants.Article.STATE_DRAFT)
                .orderByDesc("create_time");
        IPage<TbArticle> tbArticleIPage = articleMapper.selectPage(page, wrapper);

        return ResponseResult.success("获取" + label + "分类成功").setData(tbArticleIPage);
    }

    /*获取分类列表*/
    @Override
    public ResponseResult getlistCategories() {
        List<TbCategories> list = articleMapper.getlistCategories();
        return ResponseResult.success("获取分类列表成功！").setData(list);
    }

    /*
    * 获取标签云
    * */
    @Override
    public ResponseResult getArticleLabels(int size) {
        List<TbLabels> labelsAll = labelsMapper.getLabelsAll(size);
        return ResponseResult.success().setData(labelsAll);
    }


}
