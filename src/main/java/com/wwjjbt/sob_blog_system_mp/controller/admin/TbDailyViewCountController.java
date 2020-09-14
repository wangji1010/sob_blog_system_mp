package com.wwjjbt.sob_blog_system_mp.controller.admin;


import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
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
@RequestMapping("/admin/web_size_info")
public class TbDailyViewCountController {

    @GetMapping("/title")
    public ResponseResult getWebSizeTile(){

        return null;
    }

    @PutMapping("/title")
    public ResponseResult upWebSizeTile(@RequestParam("title") String title){

        return null;
    }

    @GetMapping("/seo")
    public ResponseResult getSeoInfo(){

        return null;
    }

    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords")String keywords
                                    ,@RequestParam("description")String description){

        return null;
    }

    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount(){

        return null;
    }
}

