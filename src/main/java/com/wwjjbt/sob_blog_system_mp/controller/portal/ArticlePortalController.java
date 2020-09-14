package com.wwjjbt.sob_blog_system_mp.controller.portal;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbArticleService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/portal/article")
public class ArticlePortalController {


    @Resource
    private TbArticleService articleService;


    /*获取分类列表*/
    @GetMapping("/article/categories")
    public ResponseResult listCategories(){
        return articleService.getlistCategories();
    }

    /*
    权限所有用户
    状态：已发布state=1
    置顶：使用其他接口获取
    * 获取所有文章，分页
    * */
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticle(@PathVariable("page")int page,@PathVariable("size")int size){

        return articleService.getListArticle(page,size,null,null, Constrants.Article.STATE_PULISH);

    }

    /*
    * 文章某类型，分页查询
    * */
    @GetMapping("/list/{categoryId}/{page}/{size}")
    public ResponseResult listArticleByCategory(@PathVariable("categoryId")String categoryId,@PathVariable("page")int page,@PathVariable("size")int size){

        return articleService.getListArticle(page,size,null,categoryId,"1");

    }
    /*
    * 获取文章详情
    * 权限：任意用户
    * 只允许获取置顶，已发布文章
    * 其他的获取需要管理权限
    * */
    @GetMapping("/{articleId}")
    public ResponseResult getArticleDetail(@PathVariable("articleId")String articleId){

        return articleService.getArticleById(articleId);

    }

    /*
    * 获取推荐内容
    * 标签：有一个或者5个以内
    * 从里面随机拿一个标签，达到每次推荐不雷同
    * 如果没有相关的文章，就根据表查询出来
    * */
    @GetMapping("/recommend/{articleId}/{size}")
    public ResponseResult getRecommendArticle(@PathVariable("articleId")String articleId,@PathVariable("size")int zize){

        return articleService.listRecommendArticleId(articleId,zize);
    }

    //获取标签云，用户点击标签就会获取相关文章列表
    @GetMapping("/label/{size}")
    public ResponseResult getlabels(@PathVariable("size")int size){
        return articleService.getArticleLabels(size);
    }

    /*
    获取某个分类的文章列表（分页）
    * */
    @GetMapping("/list/label/{label}/{pages}/{size}")
    public ResponseResult listArticleByLabel(@PathVariable("label")String label,
                                             @PathVariable("pages")int pages,
                                             @PathVariable("size")int size){
        return articleService.listArticles(pages,size,label);
    }

    /*
    *获取置顶
    * */
    @GetMapping("/top")
    public ResponseResult getTopArticle(){

        return articleService.getTopArticle();
    }
}
