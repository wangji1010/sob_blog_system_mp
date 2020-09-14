package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.pojo.TbCategories;
import com.wwjjbt.sob_blog_system_mp.mapper.TbCategoriesMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Service
public class TbCategoriesServiceImpl implements TbCategoriesService {



    @Autowired
    private IdWorker idWorker;

    @Resource
    private TbCategoriesMapper categoriesMapper;

    @Autowired
    private TbUserService userService;





    /*
    * 添加分类
    * 分类名称
    * 分类拼音
    * 分类顺序
    * 分类描述
    *
    * */
    @Override
    public ResponseResult addCategory(TbCategories category) {
        //检查数据
        if (TextUtils.isEmpty(category.getcName())){
            return ResponseResult.failed("分类名称不能为空");
        }
        if (TextUtils.isEmpty(category.getPinyin())){
            return ResponseResult.failed("分类拼音不能为空");
        }
        if (TextUtils.isEmpty(category.getDescription())){
            return ResponseResult.failed("分类描述不能为空");
        }

        //补全
        category.setId(idWorker.nextId()+"");
        category.setcStatus("1");
        category.setcOrder(2);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());

        //保存
        categoriesMapper.insert(category);

        return ResponseResult.success("添加分类成功！");
    }

    /*
    * 获取单个分类记录
    * */
    @Override
    public ResponseResult getCategory(String categoryId) {
        TbCategories category = categoriesMapper.selectById(categoryId);
        if (category==null){
            return ResponseResult.failed("分类不存在");
        }
        return ResponseResult.success("获取分类成功").setData(category);
    }

    /*

    * 获取分类列表
    * */
    @Override
    public ResponseResult getCategoryList() {
        //判断用户当前的角色，  普通用户和未登录的用户只能看到正常的分类（状态state=1的）
        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        TbUser user = userService.checkUser(request, response);
        if (user==null|| Constrants.User.ROLE_NORMAL.equals(user.getRoles())) {
            //只能获取到正常的分类
            QueryWrapper<TbCategories> wrapper = new QueryWrapper<>();
            wrapper.eq("c_status",1);
            wrapper.orderByDesc("create_time");
            List<TbCategories> tbCategories = categoriesMapper.selectList(wrapper);
            return ResponseResult.success("获取分类列表成功！").setData(tbCategories);
        }else {
            List<TbCategories> tbCategories = categoriesMapper.selectAll();
            return ResponseResult.success("(管理员)获取分类列表成功！").setData(tbCategories);
        }

    }

    /*
    * 修改分类
    * */
    @Override
    public ResponseResult updateCategory(String categoryId, TbCategories categories) {
        //找出
        TbCategories categoryFromDb= categoriesMapper.selectByupId(categoryId);
        if (categoryFromDb==null){
            return ResponseResult.failed("分类不存在");
        }
        //判断
        String categoryName = categories.getcName();
        if (!TextUtils.isEmpty(categoryName)){
            categoryFromDb.setcName(categoryName);
        }
        String categoryPinyin = categories.getPinyin();
        if (!TextUtils.isEmpty(categoryPinyin)){
            categoryFromDb.setPinyin(categoryPinyin);
        }
        String categoryDesc = categories.getDescription();
        if (!TextUtils.isEmpty(categoryDesc)){
            categoryFromDb.setDescription(categoryDesc);
        }
        String categoryStatus = categories.getcStatus();
        if (!TextUtils.isEmpty(categoryStatus)){
            categoryFromDb.setcStatus(categoryStatus);
        }
        Integer categoryOrder = categories.getcOrder();
        if (categoryOrder>=1){
            categoryFromDb.setcOrder(categoryOrder);
        }
        //保存
        categoriesMapper.updateByupId(categoryFromDb);
        return ResponseResult.success("更新成功！");
    }

    /*
    * 删除分类，逻辑删除
    * */
    @Override
    public ResponseResult deleteCategory(String categoryId) {
        int deleteById = categoriesMapper.deleteById(categoryId);
        if (deleteById==0){
            return ResponseResult.failed("该分类不存在");
        }

        return ResponseResult.success("删除分类成功");
    }

    @Override
    public ResponseResult getCategorysByState() {
        List<TbCategories> categorysByState = categoriesMapper.getCategorysByState();
        return ResponseResult.success("获取分类成功").setData(categorysByState);
    }
}
