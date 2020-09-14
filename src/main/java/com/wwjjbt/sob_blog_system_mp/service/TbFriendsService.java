package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbFriends;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
public interface TbFriendsService {

    ResponseResult addFriendLink(TbFriends friendLink);

    ResponseResult getFriendLinkById(String friendLinkId);

    ResponseResult getFriendLinkList();

    ResponseResult deleteFriendById(String friendLinkId);

    ResponseResult updateFriend(String friendLinkId, TbFriends friends);
}
