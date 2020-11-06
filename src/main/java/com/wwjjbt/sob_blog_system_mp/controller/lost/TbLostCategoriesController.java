package com.wwjjbt.sob_blog_system_mp.controller.lost;


import com.wwjjbt.sob_blog_system_mp.pojo.TbLostCategories;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostCategoriesService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/lost/cate")
public class TbLostCategoriesController {

    @Autowired
    private TbLostCategoriesService lostCategoriesService;

    /*
    *
    * */
    @GetMapping("/categoryList")
    public ResponseResult getCateAll(){
        return lostCategoriesService.getCate();
    }

    /*
    * 分页查询分类列表
    * */
    @GetMapping("/category/{page}/{size}")
    public ResponseResult getCatePage(@PathVariable("page") int page,@PathVariable("size") int size){

        return lostCategoriesService.getCatePage(page,size);
    }

    /*
    * 根据id查看分类信息
    * */
    @GetMapping("/category/{id}")
    public ResponseResult getCateById(@PathVariable("id") String id){

        return lostCategoriesService.getCateById(id);
    }

    /*
    * 根据名称查看分类信息
    * */
    @GetMapping("/category")
    public ResponseResult getCateByName(@RequestParam("cateName") String cateName){

        return lostCategoriesService.getCateByName(cateName);
    }

    /*
    * 删除分类
    * */
    @DeleteMapping("/delete_category")
    public ResponseResult deleteCateById(@RequestParam("id") String id){
        return lostCategoriesService.deleteCateById(id);
    }

    /*
    * 逻辑删除
    * */
    @PutMapping("/delete_category/{id}")
    public ResponseResult updateStatusById(@PathVariable("id") String id){
        return lostCategoriesService.updateStatusById(id);
    }

    /*
    * 修改分类信息
    * */
    @PutMapping("/update_category")
    public ResponseResult updateCate(@RequestParam("id") String id,@RequestBody TbLostCategories lostCategories){

        return lostCategoriesService.updateCate(id,lostCategories);
    }

    /*
    * 添加分类
    * */
    @PostMapping("/insert_category")
    public ResponseResult insertCate(@RequestBody TbLostCategories lostCategories){

        return lostCategoriesService.insertCate(lostCategories);
    }



}

