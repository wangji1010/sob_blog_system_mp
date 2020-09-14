package com.wwjjbt.sob_blog_system_mp.controller.portal;

import com.wwjjbt.sob_blog_system_mp.pojo.TbComment;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portal/comment")
public class CommentPortalController {


    @Autowired
    private TbCommentService commentService;

    /*
     * 添加Comment
     * */
    @PostMapping
    public ResponseResult uploadComment(@RequestBody TbComment comment){

        return commentService.addComment(comment);
    }

    /*
     * 删除Comment
     * */
    @DeleteMapping("/{commentId}")
    public ResponseResult delComment(@PathVariable("commentId") String commentId){

        return commentService.deleteCommendById(commentId);
    }

    /*
     * 获取评论列表
     * */
    @GetMapping("/list/{articleId}/{pages}/{size}")
    public ResponseResult listComments(@PathVariable("articleId") String articleId
                                        ,@PathVariable("pages")int page,@PathVariable("size") int size){

        return commentService.listCommend(articleId,page,size);
    }
}
