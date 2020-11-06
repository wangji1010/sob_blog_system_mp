package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostWxuserMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostPost;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostPostMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbLostWxuserService;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@Slf4j
@Service
public class TbLostPostServiceImpl extends ServiceImpl<TbLostPostMapper, TbLostPost> implements TbLostPostService {

    @Autowired
    private TbLostPostMapper lostPostMapper;

    @Autowired
    private TbUserService userService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TbLostWxuserMapper lostWxuserMapper;

    @Override
    public ResponseResult insertPost(TbLostPost lostPost
            , String key
            , HttpServletRequest request
            , HttpServletResponse response) throws JSONException {

        System.out.println(key);
        //检查用户是否登录
        Object isLogin = redisUtil.get(key);
        if (isLogin!=null){
            //获取openId 查询出用户，
            JSONObject jsonObject = new JSONObject((String) isLogin);
            String openId = (String) jsonObject.get("openid");
            TbLostWxuser tbLostWxuser = lostWxuserMapper.selectByOS(openId);
            //检查数据
            String title = lostPost.getTitle();
            if (TextUtils.isEmpty(title)) {
                return ResponseResult.failed("帖子的标题不能为空");
            }
            String summary = lostPost.getSummary();
            if (TextUtils.isEmpty(summary)) {
                return ResponseResult.failed("帖子的描述不能为空");
            }
            String content = lostPost.getContent();
            if (TextUtils.isEmpty(content)) {
                return ResponseResult.failed("帖子的内容不能为空");
            }
            //补充数据
            lostPost.setId(idWorker.nextId() + "");
            lostPost.setState(1);
            lostPost.setTop(0);
            lostPost.setUserId(tbLostWxuser.getId());
            lostPost.setUserName(tbLostWxuser.getNickName());
            lostPost.setUserAvatar(tbLostWxuser.getUserAvatar());
            lostPost.setCreateTime(new Date());
            lostPost.setUpdateTime(new Date());
            lostPost.setViewCount(0);

            //入库
            lostPostMapper.insert(lostPost);

            return ResponseResult.success("添加帖子成功!");

        }else {

        }


        return null;
    }

    /*
     * 删除帖子
     * */
    @Override
    public ResponseResult deletePostById(String id) {
        TbLostPost tbLostPost = lostPostMapper.selectById(id);
        if (tbLostPost == null) {
            return ResponseResult.failed("该帖子不存在");
        }
        lostPostMapper.deleteById(id);
        return ResponseResult.success("帖子删除成功");
    }

    /*
     * 逻辑删除帖子
     * */
    @Override
    public ResponseResult updatePostState(String id) {
        TbLostPost tbLostPost = lostPostMapper.selectById(id);
        if (tbLostPost == null) {
            return ResponseResult.failed("该帖子不存在");
        }
        lostPostMapper.updatePostState(id);
        return ResponseResult.success("帖子删除成功(逻辑删除)");
    }

    /*
     * 分页查询帖子
     * */
    @Override
    public ResponseResult selectPostByPage(int page, int size) {
        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);
        Page<TbLostPost> page1 = new Page<>(checkPage, checkSize);
        QueryWrapper<TbLostPost> wrapper = new QueryWrapper<>();
        wrapper.eq("state", "1");
        wrapper.orderByDesc("create_time");
        IPage<TbLostPost> tbLostPostIPage = lostPostMapper.selectPage(page1, wrapper);
        return ResponseResult.success("查询成功").setData(tbLostPostIPage);
    }

    /*
     * 更新帖子
     * */
    @Override
    public ResponseResult updatePost(String id, TbLostPost lostPost) {

        TbLostPost tbLostPostFromDB = lostPostMapper.selectById(id);
        if (tbLostPostFromDB == null) {
            return ResponseResult.failed("该帖子不存在");
        }
        String title = lostPost.getTitle();
        if (!TextUtils.isEmpty(title)) {
            tbLostPostFromDB.setTitle(title);
        }
        String summary = lostPost.getSummary();
        if (!TextUtils.isEmpty(summary)) {
            tbLostPostFromDB.setSummary(summary);
        }
        String content = lostPost.getContent();
        if (!TextUtils.isEmpty(content)) {
            tbLostPostFromDB.setContent(content);
        }
        int state = lostPost.getState();
        if (state >= 1 || state <= 3) {
            tbLostPostFromDB.setState(state);
        }
        int top = lostPost.getTop();
        if (top == 0 || top == 1) {
            tbLostPostFromDB.setTop(top);
        }
        tbLostPostFromDB.setUpdateTime(new Date());
        //更新
        lostPostMapper.updateById(tbLostPostFromDB);

        return ResponseResult.success("更新成功");
    }

    /*
     * 查询帖子详情
     * */
    @Override
    public ResponseResult selectOneById(String id) {
        TbLostPost tbLostPost = lostPostMapper.selectById(id);
        if (tbLostPost == null) {
            return ResponseResult.failed("没有该条帖子");
        }
        //将 浏览量 +1
        //先获取 viewcount ++ 之后写入数据库
        Integer viewCount = tbLostPost.getViewCount();
        Integer count = viewCount + 1;
        log.info("=====view====" + count);
        lostPostMapper.updateViewCount(id, count);
        return ResponseResult.success("查询成功").setData(tbLostPost);
    }

    /*
     * 置顶帖子
     * */
    @Override
    public ResponseResult updateTopById(String id
            , HttpServletRequest request
            , HttpServletResponse response) {

        //检查帖子和用户是否是同一个
        //检查用户是否登录
        TbUser tbUser = userService.checkUser(request, response);
        log.info("===============================================");
        System.out.println(tbUser);
        if (tbUser == null) {
            return ResponseResult.failed("用户未登录");
        }
        //检查是否存在该文章
        TbLostPost tbLostPost = lostPostMapper.selectById(id);
        if (tbLostPost == null) {
            return ResponseResult.failed("没有该条帖子");
        }
        //判断文章的发布人是否是当前用户
        if (!tbUser.getId().equals(tbLostPost.getUserId())) {
            return ResponseResult.failed("当前用户非法操作");
        }
        //判断当前是否已经置顶，如果未置顶则置顶反则取消置顶
        Integer top = tbLostPost.getTop();
        if (top != 1) {
            //置顶
            lostPostMapper.updateTopById(id);
            return ResponseResult.success("置顶成功");
        } else {
            lostPostMapper.updateLowById(id);
            return ResponseResult.success("取消置顶");
        }

    }

    /*
     * 查询置顶的帖子
     * */
    @Override
    public ResponseResult selectTop() {

        return ResponseResult.success("查询成功").setData(lostPostMapper.selectTop());
    }


}
