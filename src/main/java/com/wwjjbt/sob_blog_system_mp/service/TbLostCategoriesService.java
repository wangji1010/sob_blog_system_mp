package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostCategories;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

import java.util.ArrayList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
public interface TbLostCategoriesService extends IService<TbLostCategories> {
            ResponseResult getCate();
            
    ResponseResult getCatePage(int page, int size);

    ResponseResult getCateById(String id);

    ResponseResult getCateByName(String cateName);

    ResponseResult insertCate(TbLostCategories lostCategories);

    ResponseResult deleteCateById(String id);

    ResponseResult updateStatusById(String id);

    ResponseResult updateCate(String id, TbLostCategories lostCategories);
}
