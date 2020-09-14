package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLooper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLooperMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLooperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Service
public class TbLooperServiceImpl implements TbLooperService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbLooperMapper looperMapper;

    /*
     * 添加轮播图
     * */
    @Override
    public ResponseResult addLooper(TbLooper looper) {
        //检查数据
        String title = looper.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.failed("标题不能为空");
        }
        String targetUrl = looper.getTargetUrl();
        if (TextUtils.isEmpty(targetUrl)) {
            return ResponseResult.failed("跳转链接不可以为空！");
        }
        String imageUrl = looper.getImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            return ResponseResult.failed("图片地址不能为空！");
        }
        //补充数据
        looper.setId(idWorker.nextId() + "");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());

        //保存数据
        looperMapper.insert(looper);
        return ResponseResult.success("添加轮播图成功");
    }


    /*
    * 获取单个loop
    * */
    @Override
    public ResponseResult getLoopById(String loopId) {
        TbLooper tbLooper = looperMapper.selectById(loopId);
        if (tbLooper==null){
            return ResponseResult.failed("轮播图不存在！");
        }

        return ResponseResult.success("获取成功").setData(tbLooper);
    }

    @Autowired
    private TbUserService userService;
    /*
    * 获取loop列表
    * */
    @Override
    public ResponseResult getLoopList() {
        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        TbUser user = userService.checkUser(request, response);
        if (user==null|| Constrants.User.ROLE_NORMAL.equals(user.getRoles())) {
            //只能获取到正常的分类
            QueryWrapper<TbLooper> wrapper = new QueryWrapper<>();
            wrapper.eq("state",1);
            wrapper.orderByDesc("create_time");
            List<TbLooper> tbTbLooper = looperMapper.selectList(wrapper);
            return ResponseResult.success("获取轮播图列表成功！").setData(tbTbLooper);
        }else {
            ArrayList<TbLooper> tbTbLooper = looperMapper.selectAll();
            return ResponseResult.success("(管理员)获取轮播列表成功！").setData(tbTbLooper);
        }
    }

    /*
    * 删除loop
    * */
    @Override
    public ResponseResult deleteLoopById(String loopId) {
        int i = looperMapper.deleteById(loopId);
        if (i==0){
            return ResponseResult.failed("删除失败！");
        }
        return ResponseResult.success("删除成功");
    }

    /*
    * 修改loop
    * */
    @Override
    public ResponseResult updateLoop(String loopId, TbLooper looper) {
        //找出来
        TbLooper tbLooper = looperMapper.selectById(loopId);
        //不可以为空的判空
        if (tbLooper==null) {
            return ResponseResult.failed("轮播图不存在");
        }
        String title = looper.getTitle();
        if (!TextUtils.isEmpty(title)) {
            tbLooper.setTitle(title);
        }
        String targetUrl = looper.getTargetUrl();
        if (!TextUtils.isEmpty(targetUrl)) {
            tbLooper.setTargetUrl(targetUrl);
        }
        String imageUrl = looper.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            tbLooper.setImageUrl(imageUrl);
        }
        Integer lOrder = looper.getLOrder();
        if (lOrder>=1) {
            tbLooper.setLOrder(lOrder);
        }
        String state = looper.getState();
        if (!TextUtils.isEmpty(state)) {
            tbLooper.setState(state);
        }
        tbLooper.setUpdateTime(new Date());
       looperMapper.updateById(tbLooper);
        return ResponseResult.success("轮播图更新成功");
    }
}
