package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */

public interface TbCategoriesService{

    ResponseResult addCategory(TbCategories category);

    ResponseResult getCategory(String categoryId);

    ResponseResult getCategoryList();

    ResponseResult updateCategory(String categoryId, TbCategories categories);

    ResponseResult deleteCategory(String categoryId);

    ResponseResult getCategorysByState();
}
