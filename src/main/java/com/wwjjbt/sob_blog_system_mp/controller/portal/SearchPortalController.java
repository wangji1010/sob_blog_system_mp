package com.wwjjbt.sob_blog_system_mp.controller.portal;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.SearchService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/*
* 搜索
* */
@RestController
@RequestMapping("/portal/search")
public class SearchPortalController {

    @Resource
    private SearchService searchService;

    @GetMapping("/{keywords}/{pages}/{size}")
    public ResponseResult search(@PathVariable("keywords") String keywords
            , @PathVariable("pages") int pages
            , @PathVariable("size") int size
            ,@RequestParam(value = "categoryId",required = false) String categoryId
            ,@RequestParam(value = "sort",required = false) String sort
                                 ) throws IOException {
        return searchService.search(keywords, pages, size,categoryId,sort);
    }
}
