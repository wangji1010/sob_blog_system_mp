package com.wwjjbt.sob_blog_system_mp.controller.lost;


import com.wwjjbt.sob_blog_system_mp.pojo.TbLostComment;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-10-26
 */
@RestController
@RequestMapping("/lost/comment")
public class TbLostCommentController {

    @Autowired
    private TbLostCommentService lostCommentService;

    //添加评论
    @PostMapping("/addComment/{key}")
    public ResponseResult addComment(@PathVariable("key") String key, @RequestBody TbLostComment lostComment) throws JSONException {
        return lostCommentService.addComment(key,lostComment);
    }
    //查询评论数量
    @GetMapping("/count/{id}")
    public ResponseResult getCount(@PathVariable("id") String id){
        return lostCommentService.getCount(id);
    }

    //查询回复评论的数量
    @GetMapping("/replyCount/{parentId}")
    public ResponseResult getReplyCount(@PathVariable("parentId") String parentId){
        return lostCommentService.getReplyCount(parentId);
    }

     //获取评论列表
    @GetMapping("/{id}/{page}/{size}")
    public ResponseResult getCommentList(@PathVariable("id") String id
            ,@PathVariable("page") int page
            ,@PathVariable("size") int size){

        return lostCommentService.getCommentList(id,page,size);
    }

    @GetMapping("/replyList/{parentId}")
    public ResponseResult getReplyList(@PathVariable("parentId") String parentId){
        return lostCommentService.getReplyList(parentId);
    }
}

