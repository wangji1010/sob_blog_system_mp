package com.wwjjbt.sob_blog_system_mp.controller.admin;


import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  分类api
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@RestController
@RequestMapping("/admin/category")
public class TbCategoriesController {

    @Autowired
    TbCategoriesService categoriesService;


    /*
    * 指获取可用的分类
    * */
    @GetMapping("/getByState")
    public ResponseResult getCategorysByState(){
      return   categoriesService.getCategorysByState();
    }

    /*
    * 添加分类
    * 需要管理员权限
    * */
    @PreAuthorize("@permission.admin()")//判断权限
    @PostMapping
    public ResponseResult addCategory(@RequestBody TbCategories category){

        return categoriesService.addCategory(category);
    }

    /*
     * 删除
     * */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{categoryId}")
    public ResponseResult delCategory(@PathVariable("categoryId") String categoryId){
        return categoriesService.deleteCategory(categoryId);
    }

    /*
     * 修改
     *
     * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId") String categoryId,@RequestBody TbCategories categories){
        return categoriesService.updateCategory(categoryId,categories);
    }

    /*
     * 获取分类,单条记录
     * 使用，修改的时候获取
     * 需要管理员权限
     * */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{categoryId}")
    public ResponseResult queryCategory(@PathVariable("categoryId") String categoryId){
        return categoriesService.getCategory(categoryId);
    }

    /*
     * 获取分类,所有记录
     * 需要管理员权限
     * */

    @GetMapping("/list")
    public ResponseResult queryAll(){

        return categoriesService.getCategoryList();
    }



}

