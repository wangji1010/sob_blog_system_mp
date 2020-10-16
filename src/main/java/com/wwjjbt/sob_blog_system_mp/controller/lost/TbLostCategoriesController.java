package com.wwjjbt.sob_blog_system_mp.controller.lost;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/lost/cate")
public class TbLostCategoriesController {
    @GetMapping("/test")
    public String test(){

        return "测试失物接口";
    }
}

