package com.wwjjbt.sob_blog_system_mp.controller.lost;


import com.sun.org.apache.regexp.internal.RE;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostArticle;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostArticleService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *失物
 * @author wangji
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/lost/article")
public class TbLostArticleController {

    @Autowired
    private TbLostArticleService lostArticleService;

    /*
    * 发布 失物内容
    */
    @PostMapping("/insert/{key}")
    public ResponseResult insertArticle(@RequestBody TbLostArticle lostArticle,
                                        @PathVariable("key") String key
                                        ) throws JSONException {
        return lostArticleService.insertArticle(lostArticle,key);
    }

    /*
    * 逻辑删除 文章
    * 点击 删除  再点击取消删除
    * */
    @PutMapping("/updateState/{id}")
    public ResponseResult updateByState(@PathVariable("id") String id){

        return lostArticleService.updateByState(id);
    }

    /*
    * 正式删除记录
    * */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteByID(@PathVariable("id") String id){
        return lostArticleService.deleteByID(id);
    }

    /*
    * 查询详情
    * */
    @GetMapping("/select/{id}")
    public ResponseResult selectArticleById(@PathVariable("id") String id
            ){
        return lostArticleService.selectArticleById(id);
    }

    /*
    * 查询，分页查询
    *
    * */
    @GetMapping("/select/{page}/{size}")
    public ResponseResult selectByPage(@PathVariable("page") int page
            ,@PathVariable("size") int size){
        return lostArticleService.selectByPage(page,size);
    }


    /*
     * 查询，分页查询
     * 传递 一个 type 如果为 1 之查看丢失  2 拾到
     * */
    @GetMapping("/selectByLF/{page}/{size}/{type}")
    public ResponseResult selectByLF(@PathVariable("page") int page
            ,@PathVariable("size") int size,@PathVariable("type") int type){
        return lostArticleService.selectByLF(page,size,type);
    }



    /*
    * 查询当前用户发的失物
    * */
    @GetMapping("/selectByUser")
    public ResponseResult selectByUser(){
        return lostArticleService.selectByUser();
    }

}

