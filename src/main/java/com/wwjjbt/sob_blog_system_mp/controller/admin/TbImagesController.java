package com.wwjjbt.sob_blog_system_mp.controller.admin;


import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbImagesService;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@RestController
@RequestMapping("/admin/images")
public class TbImagesController {

    @Autowired
    private TbImagesService imagesService;


    /*
    * 添加img
    * 图片上传（文件）
    * 使用对象存储
    * 使用Nginx + fast DFS===>处理文件 访问  上传
    * */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult uploadImg(HttpServletRequest request,HttpServletResponse response, @RequestParam("file")MultipartFile file){

        return imagesService.upLoadImage(request,response,file);
    }

    /*
    * 删除img
    * */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{imageId}")
    public ResponseResult delImg(@PathVariable("imageId") String imgId){

        return imagesService.deleteImageById(imgId);
    }

    /*
    * 修改img
    * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{imageId}")
    public ResponseResult updateImg(@PathVariable("imageId") String imgId){

        return null;
    }


    /*
    * 查询img单挑
    * */
//    @PreAuthorize("@permission.admin()")
    @GetMapping("/{imageId}")
    public void getImg(HttpServletResponse response, @PathVariable("imageId") String imgId){

      imagesService.getImage(response,imgId);
    }

    /*
    * 查询img列表
    * */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{pages}/{size}")
    public ResponseResult listImgs(@PathVariable("pages") int page,@PathVariable("size") int size){

        return imagesService.getImageList(page,size);
    }

}

