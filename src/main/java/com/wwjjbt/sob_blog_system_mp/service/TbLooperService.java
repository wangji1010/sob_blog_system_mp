package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.pojo.TbLooper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
public interface TbLooperService  {

    ResponseResult addLooper(TbLooper looper);

    ResponseResult getLoopById(String loopId);

    ResponseResult getLoopList();

    ResponseResult deleteLoopById(String loopId);

    ResponseResult updateLoop(String loopId, TbLooper looper);
}
