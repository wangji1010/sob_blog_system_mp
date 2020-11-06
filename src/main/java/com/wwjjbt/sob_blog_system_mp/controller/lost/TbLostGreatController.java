package com.wwjjbt.sob_blog_system_mp.controller.lost;


import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostGreatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/lost/great")
public class TbLostGreatController {

    /**
    *
    *   1、功能描述：一个用户对同一文章只能点赞一次，第二次就是取消赞
        2、建立一个点赞表great，字段有文章ID（aid），点赞用户ID（uid）
        3、当有用户进行点赞行为时，使用aid和uid搜索点赞表。
        *
        若有该记录，则表示用户已经点过赞，本次点击是取消点赞行为，故删除great表中的该条记录，同时将该文章的点赞数减1。
        若无该记录，则表示用户是要点赞，故在great表中添加该记录，同时该文章的点赞数加1。
    * */

    @Autowired
    private TbLostGreatService lostGreatService;

    @PostMapping("/point/{a_id}")
    public ResponseResult selectPoint(@PathVariable("a_id") String a_id ){
        return lostGreatService.selectPoint(a_id);
    }

    @GetMapping("/countGreate/{a_id}")
    public ResponseResult selectCount(@PathVariable("a_id") String a_id){
        return lostGreatService.selectCount(a_id);
    }

}

