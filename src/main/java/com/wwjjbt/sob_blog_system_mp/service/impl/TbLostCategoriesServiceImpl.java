package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.pojo.TbImages;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostCategories;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostCategoriesMapper;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@Service
public class TbLostCategoriesServiceImpl extends ServiceImpl<TbLostCategoriesMapper, TbLostCategories> implements TbLostCategoriesService {

    @Autowired
    private TbLostCategoriesMapper lostCategoriesMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult getCate() {
        QueryWrapper<TbLostCategories> wrapper = new QueryWrapper<>();
        wrapper.eq("status",1);
        wrapper.orderByAsc("create_time");
        wrapper.last("limit 10");
        List<TbLostCategories> tbLostCategories = lostCategoriesMapper.selectList(wrapper);
        return ResponseResult.success("获取前十条分类成功！").setData(tbLostCategories);
    }



    /*
    * 分页查询
    * */
    @Override
    public ResponseResult getCatePage(int page, int size) {

        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);
        Page<TbLostCategories> page1 = new Page<>(checkPage,checkSize);
        QueryWrapper<TbLostCategories> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.eq("status",1);
        IPage<TbLostCategories> selectPage = lostCategoriesMapper.selectPage(page1, wrapper);

        return ResponseResult.success().setData(selectPage);
    }

    /*
    * 查看分类单条记录
    * */
    @Override
    public ResponseResult getCateById(String id) {
        QueryWrapper<TbLostCategories> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        TbLostCategories tbLostCategories = lostCategoriesMapper.selectOne(wrapper);
        if (tbLostCategories==null){
            return ResponseResult.failed("找不到分类");
        }else {
            return ResponseResult.success("获取分类详情").setData(tbLostCategories);
        }

    }

    /*
    * 根据名称模糊查询
    * */
    @Override
    public ResponseResult getCateByName(String cateName) {
        if (TextUtils.isEmpty(cateName)){
            return ResponseResult.failed("请输入分类名称");
        }
        QueryWrapper<TbLostCategories> wrapper = new QueryWrapper<>();
        wrapper.like("l_name",cateName);
        wrapper.orderByDesc("create_time");
        List<TbLostCategories> list = lostCategoriesMapper.selectList(wrapper);

        return ResponseResult.success("查询成功").setData(list);
    }

    /*
    * 插入分类
    * 默认分类状态为1 可用
    * 排序 默认 1
    * */
    @Override
    public ResponseResult insertCate(TbLostCategories lostCategories) {
        //检查数据
        String lName = lostCategories.getLName();
        if (TextUtils.isEmpty(lName)){
            return ResponseResult.failed("分类名称不能为空");
        }
        //判断是否在数据库当中重复
        TbLostCategories categoriesFromDB = lostCategoriesMapper.selectByName(lName);
        if (categoriesFromDB!=null){
            return ResponseResult.failed("已经有这个分类了");
        }
        lostCategories.setId( idWorker.nextId()+"");
        lostCategories.setCreateTime(new Date());
        lostCategories.setUpdateTime(new Date());
        lostCategoriesMapper.insert(lostCategories);
        return ResponseResult.success("添加分类成功");
    }

    /*
    * 删除分类
    * */
    @Override
    public ResponseResult deleteCateById(String id) {
        TbLostCategories tbLostCategories = lostCategoriesMapper.selectById(id);
        if (tbLostCategories==null){
            return ResponseResult.failed("该分类不存在");
        }
        lostCategoriesMapper.deleteById(id);
        return ResponseResult.success("分类删除成功!");
    }

    /*
    * 逻辑删除
    * */
    @Override
    public ResponseResult updateStatusById(String id) {
        TbLostCategories lostCategoriesFromDB = lostCategoriesMapper.selectById(id);
        if (lostCategoriesFromDB==null){
            return ResponseResult.failed("该分类不存在");
        }
        lostCategoriesFromDB.setStatus(0);
        lostCategoriesMapper.updateById(lostCategoriesFromDB);
        return ResponseResult.success("删除成功!(逻辑删除)");
    }

    /*
    * 更新分类信息
    * */
    @Override
    public ResponseResult updateCate(String id, TbLostCategories lostCategories) {
        //判断是否存在分类
        TbLostCategories lostCategoriesFromDB = lostCategoriesMapper.selectById(id);
        if (lostCategoriesFromDB==null){
            return ResponseResult.failed("该分类不存在");
        }
        //判断数据
        String lName = lostCategories.getLName();
        if (!TextUtils.isEmpty(lName)){
            lostCategoriesFromDB.setLName(lName);
        }
        String description = lostCategories.getDescription();
        if (!TextUtils.isEmpty(description)){
            lostCategoriesFromDB.setDescription(description);
        }
        Integer order = lostCategories.getOrder();
        if (order>0){
            lostCategoriesFromDB.setOrder(order);
        }
        String pinyin = lostCategories.getPinyin();
        if (!TextUtils.isEmpty(pinyin)){
            lostCategoriesFromDB.setPinyin(pinyin);
        }
        Integer status = lostCategories.getStatus();
        if (status==0||status==1){
            lostCategoriesFromDB.setStatus(status);
        }
        lostCategoriesFromDB.setUpdateTime(new Date());

        lostCategoriesMapper.updateById(lostCategoriesFromDB);

        return ResponseResult.success("信息更新成功");
    }
}
