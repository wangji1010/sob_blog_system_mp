package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostImage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-11-01
 */
public interface TbLostImageService extends IService<TbLostImage> {

    String uploadImage(String key, MultipartFile file) throws JSONException;

    void getImageById(HttpServletResponse response, String imageId);
}
