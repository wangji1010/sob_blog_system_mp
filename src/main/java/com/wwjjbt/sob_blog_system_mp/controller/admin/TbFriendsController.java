package com.wwjjbt.sob_blog_system_mp.controller.admin;


import com.wwjjbt.sob_blog_system_mp.pojo.TbFriends;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@RestController
@RequestMapping("/admin/friend_link")
public class TbFriendsController {
    @Autowired
   private TbFriendsService friendsService;

    /*
     * 添加link
     * */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult uploadLink(@RequestBody TbFriends friendLink){

        return friendsService.addFriendLink(friendLink);
    }

    /*
     * 删除link
     * */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{friendLinkId}")
    public ResponseResult delLink(@PathVariable("friendLinkId") String friendLinkId){

        return friendsService.deleteFriendById(friendLinkId);
    }

    /*
     * 修改link
     * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{friendLinkId}")
    public ResponseResult updateLink(@PathVariable("friendLinkId") String friendLinkId,
                                     @RequestBody TbFriends friends){

        return friendsService.updateFriend(friendLinkId,friends);
    }


    /*
     * 查询link单挑
     * */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{friendLinkId}")
    public ResponseResult getLink(@PathVariable("friendLinkId") String friendLinkId){

        return friendsService.getFriendLinkById(friendLinkId);
    }

    /*
     * 查询link列表
     * */
//    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listLinks(){

        return friendsService.getFriendLinkList();
    }

}

