package com.wwjjbt.sob_blog_system_mp.service.impl;

public class CheckPageSize {
    static int checkPage(int page){
        if (page<=0){
            page=1;
        }
        return page;
    }
    static int checkSize(int size){
        if (size<=0){
            size=5;
        }
        return size;
    }
}
