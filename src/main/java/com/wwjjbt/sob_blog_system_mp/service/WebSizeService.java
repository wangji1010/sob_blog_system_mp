package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

public interface WebSizeService {
    ResponseResult getWebSizeTile();

    ResponseResult putWebSizeTile(String title);

    ResponseResult getSeoInfo();

    ResponseResult putSeoInfo(String keywords, String description);

    ResponseResult getSizeViewCount();

    void updateViewCount();
}
