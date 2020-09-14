package com.wwjjbt.sob_blog_system_mp.controller.admin;


import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbCommentService;
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
@RequestMapping("/admin/comment")
public class TbCommentController {


    @Autowired
    private TbCommentService commentService;

    /*
     * 删除Comment
     * */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("@permission.admin()")
    public ResponseResult delComment(@PathVariable("commentId") String commentId){

        return commentService.deleteCommendById(commentId);
    }

    /*
     * 修改img
     * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{commentId}")
    public ResponseResult updateComment(@PathVariable("commentId") String commentId){

        return null;
    }



    /*
     * 查询列表
     * */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{article_id}")
    public ResponseResult listComments(@PathVariable("article_id")String article_id,@RequestParam("pages") int page,@RequestParam("size") int size){

        return commentService.listCommends(article_id,page,size);
    }

    /*
    * 置顶评论
    * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{commentId}")
     public ResponseResult topComment(@PathVariable("commentId") String commentId){

        return commentService.topComment(commentId);
    }

}

