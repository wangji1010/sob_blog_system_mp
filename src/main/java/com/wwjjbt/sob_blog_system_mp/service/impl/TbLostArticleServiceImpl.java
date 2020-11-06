package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostWxuserMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostArticle;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostArticleMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@Service
public class TbLostArticleServiceImpl extends ServiceImpl<TbLostArticleMapper
        , TbLostArticle> implements TbLostArticleService {

    @Autowired
    private TbLostArticleMapper lostArticleMapper;

    @Autowired
    private TbUserService userService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TbLostWxuserMapper lostWxuserMapper;

    /*
     * 发布失物内容
     * */
    @Override
    public ResponseResult insertArticle(TbLostArticle lostArticle,String key) throws JSONException {

        //检查key，获取到openId,拿到用户数据
        Object isLogin = redisUtil.get(key);
        if (isLogin!=null){
            //解析出用户信息数据
            //获取openId 查询出用户，
            JSONObject jsonObject = new JSONObject((String) isLogin);
            String openId = (String) jsonObject.get("openid");
            TbLostWxuser tbLostWxuser = lostWxuserMapper.selectByOS(openId);

            //检查是否携带id ，如果不携带id则是添加
            String id = lostArticle.getId();
            //添加
            if (TextUtils.isEmpty(id)) {
                //检查数据
                checkData(lostArticle);
                // 补充数据
                // 补充用户 名称、头像 、 id
                lostArticle.setUserId(tbLostWxuser.getId());
                lostArticle.setUserName(tbLostWxuser.getNickName());
                lostArticle.setUserAvatar(tbLostWxuser.getUserAvatar());
                //补充 时间
                lostArticle.setUpdateTime(new Date());
                lostArticle.setCreateTime(new Date());
                //补充 状态 、 浏览量
                lostArticle.setState(1);
                lostArticle.setViewCount(0);
                lostArticle.setId(idWorker.nextId() + "");
                //入库
                lostArticleMapper.insert(lostArticle);

                return ResponseResult.success("发布失物招领成功");
            } else {
                // 修改数据
//            checkData(lostArticle);

                TbLostArticle tbLostArticleFromDB = lostArticleMapper.selectById(id);
                String title = lostArticle.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    tbLostArticleFromDB.setTitle(title);
                }
                String content = lostArticle.getContent();
                if (!TextUtils.isEmpty(content)) {
                    tbLostArticleFromDB.setContent(content);
                }
                Integer type = lostArticle.getType();
                if (type != null) {
                    if (type == 1 || type == 2) {
                        tbLostArticleFromDB.setType(type);
                    }
                }

                String cover = lostArticle.getCover();
                if (!TextUtils.isEmpty(cover)) {
                    tbLostArticleFromDB.setCover(cover);
                }
                String summary = lostArticle.getSummary();
                if (!TextUtils.isEmpty(summary)) {
                    tbLostArticleFromDB.setSummary(summary);
                }
                String categoryId = lostArticle.getCategoryId();
                if (!TextUtils.isEmpty(categoryId)) {
                    tbLostArticleFromDB.setCategoryId(categoryId);
                }
                Integer state = lostArticle.getState();
                if (state != null) {
                    if (state == 1 || state == 2 || state == 3) {
                        tbLostArticleFromDB.setState(state);
                    }
                }

                tbLostArticleFromDB.setUpdateTime(new Date());
                //更新入库
                lostArticleMapper.updateById(tbLostArticleFromDB);
                return ResponseResult.success("更新文章成功");
            }

        }else {

        }

       return null;

    }

    private ResponseResult checkData(TbLostArticle lostArticle) {
        //检查数据
        // 检查 标题
        String title = lostArticle.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.failed("标题不能为空");
        }
        // 检查分类
//        String categoryId = lostArticle.getCategoryId();
//        if (TextUtils.isEmpty(categoryId)) {
//            return ResponseResult.failed("分类不能为空");
//        }
        // 检查描述
        String summary = lostArticle.getSummary();
        if (TextUtils.isEmpty(summary)) {
            return ResponseResult.failed("描述不能为空");
        }

        //检查联系方式
        String contact = lostArticle.getContact();
        if (TextUtils.isEmpty(contact)) {
            return ResponseResult.failed("联系方式不能为空");
        }

        // 检查类型
        Integer type = lostArticle.getType();
        if ( type != 1 && type != 2) {
            return ResponseResult.failed("类型不能错误");
        }
        //检查内容
        String content = lostArticle.getContent();
        if (TextUtils.isEmpty(content)) {
            return ResponseResult.failed("内容不能为空");
        }
        return null;
    }

    /*
     * 逻辑删除
     * */
    @Override
    public ResponseResult updateByState(String id) {
        //检查是否存在该文章
        TbLostArticle tbLostArticle = lostArticleMapper.selectById(id);
        if (tbLostArticle == null) {
            return ResponseResult.failed("该文章不存在");
        }
        if (tbLostArticle.getState()==1){
            lostArticleMapper.updateByState(id);
            return ResponseResult.success(tbLostArticle.getState()==1 ? "删除文章成功" : "恢复文章成功") ;
        }
        if (tbLostArticle.getState()==3){
            lostArticleMapper.updateRollback(id);
            return ResponseResult.success(tbLostArticle.getState()==1 ? "删除文章成功" : "恢复文章成功") ;
        }

        return null;
    }


    private ResponseResult checkArticleIsOk(String id) {
        //检查是否存在该文章
        TbLostArticle tbLostArticle = lostArticleMapper.selectById(id);
        if (tbLostArticle == null) {
            return ResponseResult.failed("该文章不存在");
        }
        return null;
    }

    /*
     * 正式删除
     * */
    @Override
    public ResponseResult deleteByID(String id) {
        checkArticleIsOk(id);
        lostArticleMapper.deleteById(id);
        return ResponseResult.success("删除成功");
    }

    /*
     * 查询详情
     * */
    @Override
    public ResponseResult selectArticleById(String id) {
        checkArticleIsOk(id);
//        TbUser tbUser = userService.checkUser(request, response);
        //当前用户不为空，并且是管理员
//        if (tbUser!=null&&tbUser.getRoles().equals(Constrants.User.ROLE_ADMIN)){
        TbLostArticle tbLostArticle = lostArticleMapper.selectById(id);
        if (tbLostArticle==null){
            return ResponseResult.success("没有该条招领");
        }
        return ResponseResult.success("查询详情成功").setData(tbLostArticle);
//        }
//        if (tbUser==null||tbUser.getRoles().equals(Constrants.User.ROLE_NORMAL)){
//            TbLostArticle tbLostArticle = lostArticleMapper.selectById(id);
//            return ResponseResult.success("查询详情成功").setData(tbLostArticle);
//        }


    }

    /*
     * 分页查询 , 根据各个条件查询
     * 客户端
     * */
    @Override
    public ResponseResult selectByPage(int page, int size) {
        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);

        Page<TbLostArticle> page1 = new Page<>(checkPage, checkSize);
        QueryWrapper<TbLostArticle> wrapper = new QueryWrapper<>();

        wrapper.orderByDesc("create_time");
        IPage<TbLostArticle> tbLostArticleIPage = lostArticleMapper.selectPage(page1, wrapper);
        return ResponseResult.success("查询成功").setData(tbLostArticleIPage);
    }

    /*
     * 查看我的发布
     * */
    @Override
    public ResponseResult selectByUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        TbUser tbUser = userService.checkUser(request, response);
        if (tbUser != null) {
            String id = tbUser.getId();
            QueryWrapper<TbLostArticle> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",id);
            wrapper.eq("state",1);
            wrapper.orderByDesc("create_time");
            List<TbLostArticle> list = lostArticleMapper.selectList(wrapper);
            return ResponseResult.success("查询成功").setData(list);
        }

        return ResponseResult.failed("当前未登录");
    }

    /*
    * 查询根据 丢失和拾到
    * */
    @Override
    public ResponseResult selectByLF(int page, int size, int type) {

        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);

        Page<TbLostArticle> page1 = new Page<>(checkPage, checkSize);
        QueryWrapper<TbLostArticle> wrapper = new QueryWrapper<>();
        if (type==1){
            wrapper.eq("type",1);
        }else if (type==2){
            wrapper.eq("type",2);
        }

        wrapper.orderByDesc("create_time");
        IPage<TbLostArticle> tbLostArticleIPage = lostArticleMapper.selectPage(page1, wrapper);
        return ResponseResult.success("查询成功").setData(tbLostArticleIPage);


    }


}
