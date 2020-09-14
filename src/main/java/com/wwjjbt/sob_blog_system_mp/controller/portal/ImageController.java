package com.wwjjbt.sob_blog_system_mp.controller.portal;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbImagesService;
import com.wwjjbt.sob_blog_system_mp.service.TbLooperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/portal/image")
public class ImageController {


    @Resource
    private TbImagesService imagesService;
    @Resource
    private TbLooperService looperService;

    /*查询所有的轮播图片*/
    @GetMapping("/looplist")
    public ResponseResult getloopList(){
        return looperService.getLoopList();
    }

    /*
     * 查询img单挑
     * */
    @GetMapping("/{imageId}")
    public void getImg(HttpServletResponse response, @PathVariable("imageId") String imgId){

        imagesService.getImage(response,imgId);
    }
}
