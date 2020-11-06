package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLostGreat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-10-18
 */
public interface TbLostGreatService extends IService<TbLostGreat> {

    ResponseResult selectPoint(String a_id);

    ResponseResult selectCount(String a_id);
}
