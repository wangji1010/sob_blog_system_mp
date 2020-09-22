package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.mapper.TbArticleMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbArticle;
import com.wwjjbt.sob_blog_system_mp.pojo.TbComment;
import com.wwjjbt.sob_blog_system_mp.mapper.TbCommentMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Slf4j
@Service
public class TbCommentServiceImpl  implements TbCommentService {


    @Autowired
    private TbUserService userService;

    @Autowired
    private TbArticleMapper articleMapper;

    @Autowired
    private IdWorker idWorker;

    @Resource
    private TbCommentMapper commentMapper;

    @Resource
    private RedisUtil redisUtil;

    /*
    * 发表评论
    * */
    @Override
    public ResponseResult addComment(TbComment comment) {
        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        //检查用户是否登录
        TbUser user = userService.checkUser(request, response);
        if (user==null){
            return ResponseResult.ACCOUNT_NOT_LOGIN("用户未登录");
        }

        //检查内容
        String articleId = comment.getArticleId();
        if (TextUtils.isEmpty(articleId)) {
            return ResponseResult.failed("文章id不能为空");
        }
        TbArticle tbArticle = articleMapper.selectById(articleId);
        if (tbArticle==null){
            return ResponseResult.failed("文章不存在");
        }
        String content = comment.getContent();
        if (TextUtils.isEmpty(content)){
            return ResponseResult.failed("文章内容不能为空");
        }

        //补全数据
        comment.setId(idWorker.nextId()+"");
        comment.setUpdateTime(new Date());
        comment.setCreateTime(new Date());
        comment.setUserAvatar(user.getAvatar());
        comment.setUserName(user.getUserName());
        comment.setUserId(user.getId());



        //保存入库
        commentMapper.insert(comment);
        //邮件通知
        //SendMail.sendCommendNotify();
        return ResponseResult.success("评论成功");
    }

    /*
    * 获取文章的评论（分页）
    * 评论排序策略
    *   时间排序，---升序和降序
    *   置顶的在前面
    *   后发表的前n段时间会排在前面，过了时间会按照点赞量排序
    *
    * */
    //开启缓存注解
//    @Cacheable(value = "commend",key = "#articleId+#page+#size")
    @Override
    public ResponseResult listCommend(String articleId, int page, int size) {
        log.info("==============查询了评论信息=====");
        //参数检查
        QueryWrapper<TbComment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id",articleId);
        wrapper.orderByDesc("create_time");

        //如果是第一页先从缓存中拿
        String cacheJson = (String) redisUtil.get(Constrants.Commend.KEY_COMMEND_FIRST_PAGE_CACHE + articleId);
        if (TextUtils.isEmpty(cacheJson)) {

        }
        //如果有直接返回
        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);
        Page<TbComment> page1 = new Page<>(checkPage,checkSize);

        IPage<TbComment> tbCommentIPage = commentMapper.selectPage(page1, wrapper);
        return ResponseResult.success("获取评论列表成功").setData(tbCommentIPage);
    }

    /*
    * 删除评论
    *   （用户只能删除自己的评论）
    * */
//    @CacheEvict(value = "commend",key = "#commentId+#page+#size")
    @Override
    public ResponseResult deleteCommendById(String commentId) {
        //检查用户角色
        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        TbUser user = userService.checkUser(request, response);
        if (user==null){
            return ResponseResult.ACCOUNT_NOT_LOGIN("用户未登录");
        }
        //找出评论
        TbComment tbComment = commentMapper.selectById(commentId);
        if (tbComment==null) {
            return  ResponseResult.failed("评论不存在");
        }
        //当前评论是当前用户的，或者当前账户是管理员才可以删除
        if (user.getId().equals(tbComment.getUserId())||Constrants.User.ROLE_ADMIN.equals(user.getRoles())){
            //id一致，此评论是当前用户
            commentMapper.deleteById(commentId);
            return ResponseResult.success("评论删除成功");
        }else {
            return ResponseResult.PERMISSION_FORBID("权限不足");
        }


    }

    /*
    * 管理查看所有评论信息
    * */
    @Override
    public ResponseResult listCommends(String id,int page, int size) {
        int checkSize = CheckPageSize.checkSize(size);
        int checkPage = CheckPageSize.checkPage(page);
        Page<TbComment> page1 = new Page<>(checkPage,checkSize);
        QueryWrapper<TbComment> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("state");
        wrapper.eq("article_id",id);
        IPage<TbComment> tbCommentIPage = commentMapper.selectPage(page1, wrapper);

        return ResponseResult.success("获取评论列表成功（管理）").setData(tbCommentIPage);
    }

    @Override
    public ResponseResult topComment(String commentId) {
        TbComment tbComment = commentMapper.selectById(commentId);
        if (tbComment==null) {
            return ResponseResult.failed("评论不存在");
        }
        String state = tbComment.getState();
        if (Constrants.Commend.STATE_PULISH.equals(state)) {
            tbComment.setState(Constrants.Commend.STATE_TOP);
            commentMapper.updateById(tbComment);
            return ResponseResult.success("置顶成功");
        }else if (Constrants.Commend.STATE_TOP.equals(state)){
            tbComment.setState(Constrants.Commend.STATE_PULISH);
            commentMapper.updateById(tbComment);
            return ResponseResult.success("取消置顶成功");
        }else {
            return ResponseResult.failed("评论状态非法");
        }




    }
}
