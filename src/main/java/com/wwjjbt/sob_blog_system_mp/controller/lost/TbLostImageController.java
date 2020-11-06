package com.wwjjbt.sob_blog_system_mp.controller.lost;


import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-11-01
 */
@RestController
@RequestMapping("/lost/image")
public class TbLostImageController {

    @Autowired
    private TbLostImageService lostImageService;

    /*
    * 图片上传
    * */
    @PostMapping("/{key}")
    public String uploadImage(@PathVariable("key") String key
            , MultipartFile file) throws JSONException {
        return lostImageService.uploadImage(key,file);
    }


    /*
    * 访问图片
    * */
    @GetMapping("/{imageId}")
    public void getImageById(HttpServletResponse response,@PathVariable("imageId") String imageId){
        lostImageService.getImageById(response,imageId);
    }



}

