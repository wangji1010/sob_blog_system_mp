package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.pojo.TbFriends;
import com.wwjjbt.sob_blog_system_mp.mapper.TbFriendsMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbFriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Service
public class TbFriendsServiceImpl implements TbFriendsService {

    @Resource
    private TbFriendsMapper friendsMapper;
    @Resource
    private IdWorker idWorker;

    @Autowired
    private TbUserService userService;

    @Override
    public ResponseResult addFriendLink(TbFriends friendLink) {
        //判断
        String url = friendLink.getUrl();
        if (TextUtils.isEmpty(url)){
            return ResponseResult.failed("链接不能为空");
        }
        String logo = friendLink.getLogo();
        if (TextUtils.isEmpty(logo)){
            return ResponseResult.failed("logo不能为空");
        }
        String name = friendLink.getlName();
        if (TextUtils.isEmpty(name)){
            return ResponseResult.failed("链接名称不能为空");
        }
        //补全数据
        friendLink.setId(idWorker.nextId()+"");
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
        friendsMapper.insert(friendLink);
        return ResponseResult.success("添加友情链接成功");
    }

    @Override
    public ResponseResult getFriendLinkById(String friendLinkId) {
        TbFriends selectById = friendsMapper.selectById(friendLinkId);
        return ResponseResult.success("获取单挑友链成功").setData(selectById);
    }

    /*
    * 获取列表
    * 分两种清空，管理账户和普通账户
    * */
    @Override
    public ResponseResult getFriendLinkList() {

        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        TbUser user = userService.checkUser(request, response);
        if (user==null|| Constrants.User.ROLE_NORMAL.equals(user.getRoles())) {
            //只能获取到正常的分类
            QueryWrapper<TbFriends> wrapper = new QueryWrapper<>();
            wrapper.eq("l_state",1);
            wrapper.orderByDesc("l_order");
            List<TbFriends> friendsList = friendsMapper.selectList(wrapper);
            return ResponseResult.success("获取友情链接列表成功！").setData(friendsList);
        }else {
            List<TbFriends> friendsList = friendsMapper.selectAll();
            return ResponseResult.success("(管理员)获取友情链接列表成功！").setData(friendsList);
        }


    }

    /*
    * 删除
    * */
    @Override
    public ResponseResult deleteFriendById(String friendLinkId) {
        int i = friendsMapper.deleteById(friendLinkId);
        if (i==0){
            return ResponseResult.failed("友链不存在");
        }
        return ResponseResult.success("删除成功！");
    }

    /*
    * 修改
    * */
    @Override
    public ResponseResult updateFriend(String friendLinkId, TbFriends friends) {
        //查出
        TbFriends friendsFromDb = friendsMapper.selectById(friendLinkId);
        String name = friends.getlName();
        if (!TextUtils.isEmpty(name)){
            friendsFromDb.setlName(name);
        }
        String logo = friends.getLogo();
        if (!TextUtils.isEmpty(logo)){
            friendsFromDb.setLogo(logo);
        }
        String url = friends.getUrl();
        if (!TextUtils.isEmpty(url)){
            friendsFromDb.setUrl(url);
        }
        String state = friends.getlState();
        if (!TextUtils.isEmpty(state)){
            friendsFromDb.setlState(state);
        }
        Integer weight = friends.getlOrder();
        if (weight >=0){
            friendsFromDb.setlOrder(weight);
        }
        friendsMapper.updateById(friendsFromDb);
        return ResponseResult.success("更新友情连接成功");
    }
}
