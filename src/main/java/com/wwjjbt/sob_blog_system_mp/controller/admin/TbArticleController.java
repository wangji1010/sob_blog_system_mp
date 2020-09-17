package com.wwjjbt.sob_blog_system_mp.controller.admin;


import com.wwjjbt.sob_blog_system_mp.pojo.TbArticle;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@RestController
@RequestMapping("/admin/article")
public class TbArticleController {

    @Resource
    private TbArticleService articleService;
    /*
     * 添加article
     * */
//    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult uploadArticle(HttpServletRequest request, HttpServletResponse response, @RequestBody TbArticle article) throws IOException {

        return articleService.addArticle(request,response,article);
    }

    /*
     * 删除article
     * 多用户只是修改状态
     * 管理员可以删除
     * 真的删除
     * */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{articleId}")
    public ResponseResult delArticle(@PathVariable("articleId") String articleId) throws IOException {

        return articleService.deleteById(articleId);
    }

    /*
     * 修改article
     * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId") String articleId,@RequestBody TbArticle article) throws IOException {

        return articleService.updateArticleById(articleId,article);
    }


    /*
     * 查询article单挑
     * */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId") String articleId) throws IOException {

        return articleService.getArticleById(articleId);
    }

    /*
     * 查询列表
     * */
//    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{pages}/{size}")
    public ResponseResult listArticle(@PathVariable("pages") int page,@PathVariable("size") int size,
                                      @RequestParam(value = "keywords",required = false)String keywords,
                                      @RequestParam(value = "categoryId",required = false)String categoryId,
                                      @RequestParam(value = "state",required = false)String state){

        return articleService.getListArticle(page,size,keywords,categoryId,state);
    }

    /*
    *
    * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/state/{articleId}")
    public ResponseResult deleteByupdateState(@PathVariable("articleId")String articleId){

        return articleService.deleteByState(articleId);
    }

    /*
    * 置顶
    * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{articleId}")
    public ResponseResult updateState(@PathVariable("articleId")String articleId){

        return articleService.topArticle(articleId);
    }

}

