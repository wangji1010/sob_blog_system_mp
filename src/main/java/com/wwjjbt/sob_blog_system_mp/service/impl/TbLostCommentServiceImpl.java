package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostArticleMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostPostMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostWxuserMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostArticle;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostComment;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostCommentMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostPost;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbLostPostService;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-10-26
 */
@Service
public class TbLostCommentServiceImpl extends ServiceImpl<TbLostCommentMapper, TbLostComment> implements TbLostCommentService {


    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbLostPostMapper lostPostMapper;

    @Autowired
    private TbLostArticleMapper lostArticleMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TbLostWxuserMapper lostWxuserMapper;

    @Autowired
    private TbLostCommentMapper lostCommentMapper;

    /*
    * 添加评论
    * */
    @Override
    public ResponseResult addComment( String key,TbLostComment lostComment) throws JSONException {

        //拿到用户信息,前端传递key，后端从redis中取出，解析得到openid 通过o 查询出数据库的用户信息
        Object o = redisUtil.get(key);
        String nickName = null;
        String userId=null;
        String userAvatar=null;
        if (o!=null){
            //解析数据
            JSONObject jsonObject = new JSONObject((String) o);
            String openid = (String) jsonObject.get("openid");
            TbLostWxuser tbLostWxuser = lostWxuserMapper.selectByOS(openid);
            nickName = tbLostWxuser.getNickName();
            userId = tbLostWxuser.getId();
            userAvatar = tbLostWxuser.getUserAvatar();
            //查询用户信息
        }else {
            return ResponseResult.failed("身份验证失效");
        }
        String parentId = lostComment.getParentContent();
        if(!TextUtils.isEmpty(parentId)){
            //父id如果不为空则是回复别人的信息
            // 检查回复内容
            if (TextUtils.isEmpty(lostComment.getContent())){
                return ResponseResult.failed("回复内容为空！");
            }
            //补全数据
            lostComment.setId(idWorker.nextId()+"");
            lostComment.setParentContent(parentId);
            lostComment.setUserId(userId);
            lostComment.setUserName(nickName);
            lostComment.setUserAvatar(userAvatar);
            lostComment.setState("0");
            lostComment.setCreateTime(new Date());
            lostComment.setUpdateTime(new Date());

            lostCommentMapper.insert(lostComment);
            return ResponseResult.success("回复成功");
        }

        //加添
            //检查数据
        if (TextUtils.isEmpty(lostComment.getPostId())) {
            return ResponseResult.failed("文章id不能为空");
        }
        TbLostPost postResult = lostPostMapper.selectById(lostComment.getPostId());
        TbLostArticle LostResult = lostArticleMapper.selectById(lostComment.getPostId());
        if (postResult==null&&LostResult==null){
            return ResponseResult.failed("文章不存在");
        }
        if (TextUtils.isEmpty(lostComment.getContent())){
            return ResponseResult.failed("评论内容不能为空");
        }
        if (postResult!=null){
            //补充数据
            commentFull(lostComment, nickName, userId, userAvatar);

            return ResponseResult.success("帖子评论成功");
        }
        if (LostResult!=null){
            //补充数据
            commentFull(lostComment, nickName, userId, userAvatar);

            return ResponseResult.success("失物评论成功");
        }

      return null;
    }

    private void commentFull(TbLostComment lostComment, String nickName, String userId, String userAvatar) {
        lostComment.setId(idWorker.nextId()+"");
        lostComment.setCreateTime(new Date());
        lostComment.setUpdateTime(new Date());
        lostComment.setState("0");
        lostComment.setUserName(nickName);
        lostComment.setUserAvatar(userAvatar);
        lostComment.setUserId(userId);

        lostCommentMapper.insert(lostComment);
    }

    /*
    * 查询文章评论的数量
    * */
    @Override
    public ResponseResult getCount(String id) {
        int count = lostCommentMapper.selectCommentCount(id);
        return ResponseResult.success("获取数量成功").setData(count);
    }

    /*
    * 获取评论列表
    * */
    @Override
    public ResponseResult getCommentList(String id, int page, int size) {
        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);
        Page<TbLostComment> page1 = new Page<>(checkPage,checkSize);
        QueryWrapper<TbLostComment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id",id);
//        wrapper.eq("parent_content",null);
        wrapper.isNull("parent_content");
        wrapper.orderByDesc("create_time");
        IPage<TbLostComment> tbLostCommentIPage = lostCommentMapper.selectPage(page1, wrapper);

        return ResponseResult.success("查询评论列表成功").setData(tbLostCommentIPage);
    }

    /*
    * 获取回复评论的数量
    * */
    @Override
    public ResponseResult getReplyCount(String parentId) {

        int count = lostCommentMapper.selectReplyCount(parentId);

        return ResponseResult.success("查询成功").setData(count);
    }

    /**
     * 获取回复评论列表
     * */
    @Override
    public ResponseResult getReplyList(String parentId) {
        ArrayList<TbLostComment> replyList = lostCommentMapper.getReplyList(parentId);
        return ResponseResult.success("获取子评论成功").setData(replyList);
    }

    /**
     * 添加子评论
     * */



}
