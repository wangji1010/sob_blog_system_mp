package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbImages;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
public interface TbImagesService  {

    ResponseResult upLoadImage(HttpServletRequest request,HttpServletResponse response, MultipartFile file);

    void getImage(HttpServletResponse response, String imgId);

    ResponseResult getImageList(int page, int size);

    ResponseResult deleteImageById(String imgId);
}
