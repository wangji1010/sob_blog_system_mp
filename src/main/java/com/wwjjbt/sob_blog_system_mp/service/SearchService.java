package com.wwjjbt.sob_blog_system_mp.service;

import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;

import java.io.IOException;

public interface SearchService {
    ResponseResult search(String keywords, int pages, int size,String categoryId,String sort) throws IOException;
}
