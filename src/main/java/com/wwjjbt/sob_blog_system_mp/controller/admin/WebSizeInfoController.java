package com.wwjjbt.sob_blog_system_mp.controller.admin;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbFriendsService;
import com.wwjjbt.sob_blog_system_mp.service.WebSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
 * 网站信息
 * */
@RestController
@RequestMapping("/admin/webinfo")
public class WebSizeInfoController {

    @Autowired
    private WebSizeService webSizeService;

    @Autowired
    private TbFriendsService friendsService;

    /*
     * 网站标题
     * */
//    @PreAuthorize("@permission.admin()")
    @GetMapping("/title")
    public ResponseResult getTitle() {

        return webSizeService.getWebSizeTile();


    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/title")
    public ResponseResult putTitles(@RequestParam("title") String title) {

        return webSizeService.putWebSizeTile(title);

    }


    /*
     * seo信息
     * */
//    @PreAuthorize("@permission.admin()")
    @GetMapping("/seo")
    public ResponseResult getSeos() {

        return webSizeService.getSeoInfo();

    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/seo")
    public ResponseResult putSeo(@RequestParam("keywords") String keywords,
                                 @RequestParam("description") String description) {

        return webSizeService.putSeoInfo(keywords, description);

    }

//    @PreAuthorize("@permission.admin()")
    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCounts() {
        return webSizeService.getSizeViewCount();

    }

    /*
    * 统计每个页面访问量
    * 直接增加一个访问量
    * 格局ip进行过滤，可借助第三方统计工具
    * //统计信息通过redis统计，数据保存到MySQL
    * 不会每次更新到mysql，当用户获取访问量的时候会更新一次
    * 平时调用只增加redis
    *
    * redis时机，每隔页面访问的时候，如果不是从mysql中读取，写在redis里面
    * 就自增
    *
    * mysql的时机，用户读取总访问量的时候就读取redis的并且更新到mysql
    * ‘如果redis没有就读取mysql写到redis
    * */
    @PutMapping("/view_count")
    public void updateWebViewCount(){
        webSizeService.updateViewCount();
    }

}
