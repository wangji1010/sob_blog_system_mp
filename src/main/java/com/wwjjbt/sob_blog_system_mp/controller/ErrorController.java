package com.wwjjbt.sob_blog_system_mp.controller;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
    @GetMapping("/404")
    public ResponseResult page404(){
        return ResponseResult.error_404();
    }
    @GetMapping("/403")
    public ResponseResult page403(){
        return ResponseResult.error_403();
    }
    @GetMapping("/504")
    public ResponseResult page504(){
        return ResponseResult.error_504();
    }
    @GetMapping("/505")
    public ResponseResult page505(){
        return ResponseResult.error_505();
    }

}
