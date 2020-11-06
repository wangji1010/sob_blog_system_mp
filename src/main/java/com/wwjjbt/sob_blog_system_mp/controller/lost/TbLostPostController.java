package com.wwjjbt.sob_blog_system_mp.controller.lost;


import com.wwjjbt.sob_blog_system_mp.pojo.TbLostPost;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *帖子
 * @author wangji
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/lost/post")
public class TbLostPostController {

    @Autowired
    private TbLostPostService lostPostService;

    /*
    * 发布帖子
    * */
    @PostMapping("/insert")
//    @PreAuthorize("@permission.checkLogin()")
    public ResponseResult insertPost(@RequestBody TbLostPost lostPost
            ,String key
            , HttpServletRequest request
            , HttpServletResponse response) throws JSONException {

        return lostPostService.insertPost(lostPost,key,request,response);

    }

    /*
    * 删除帖子
    * */
    @DeleteMapping("/delete/post/{id}")
    @PreAuthorize("@permission.checkLogin()")
    public ResponseResult deletePostById(@PathVariable("id") String id){

        return lostPostService.deletePostById(id);
    }
    /*
    * 逻辑删除帖子
    * */
    @PutMapping("/update/{id}")
    @PreAuthorize("@permission.admin()")
    public ResponseResult updatePostState(@PathVariable("id") String id){

        return lostPostService.updatePostState(id);
    }

    /*
    * 编辑帖子
    * */
    @PutMapping("/update")
    @PreAuthorize("@permission.checkLogin()")
    public ResponseResult updatePost(@RequestParam("id") String id,@RequestBody TbLostPost lostPost){

        return lostPostService.updatePost(id,lostPost);
    }

    /*
    *查询帖子分页查询
    * */
    @GetMapping("/{page}/{size}")
    public ResponseResult selectPostByPage(@PathVariable("page") int page
            ,@PathVariable("size") int size){

        return lostPostService.selectPostByPage(page,size);
    }

    /*
    * 帖子详情
    * */
    @GetMapping("/select/{id}")
    public ResponseResult selectOneById(@PathVariable String id){
        return lostPostService.selectOneById(id);
    }

    /*
    * 置顶帖子
    * */
    @PutMapping("/updateTop/{id}")
    @PreAuthorize("@permission.checkLogin()")
    public ResponseResult updateTopById(@PathVariable String id,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return lostPostService.updateTopById(id,request,response);
    }

    /*
    * 查询置顶的帖子
    * */
    @GetMapping("/selectTop")
    public ResponseResult selectTop(){
        return lostPostService.selectTop();
    }



}

