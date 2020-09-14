package com.wwjjbt.sob_blog_system_mp.controller.portal;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbFriendsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/portal/article")
public class FriendLink {

    @Resource
    private TbFriendsService friendsService;

    @GetMapping("/linkList")
    public ResponseResult getFriendLinkList(){
        return friendsService.getFriendLinkList();
    }
}
