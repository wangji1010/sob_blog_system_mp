package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbSettingsMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbSettings;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.WebSizeService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WebSizeServiceImpl implements WebSizeService {

    @Autowired
    private TbSettingsMapper settingsMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult getWebSizeTile() {
        QueryWrapper<TbSettings> wrapper = new QueryWrapper<>();
        wrapper.eq("key", Constrants.Settings.WEB_SIZE_TITLE);
        TbSettings webTitle = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_TITLE);
        return ResponseResult.success("获取网站title成功").setData(webTitle);
    }

    @Override
    public ResponseResult putWebSizeTile(String title) {
        if (TextUtils.isEmpty(title)){
            return ResponseResult.failed("网站标题不能为空");
        }
        TbSettings webTitle = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_TITLE);
        if (webTitle==null){
            webTitle=new TbSettings();
            webTitle.setId(idWorker.nextId()+"");
            webTitle.setUpdateTime(new Date());
            webTitle.setCreateTime(new Date());
            webTitle.setKey(Constrants.Settings.WEB_SIZE_TITLE);
            webTitle.setValue(title);
            settingsMapper.insertinto(webTitle);
        }
//        webTitle.setValue(title);
        Map<String, Object> map = new HashMap<>();
        map.put("value",title);
        map.put("key","web_size_title");
        settingsMapper.updateSettingsMapper(map);
        return ResponseResult.success("网站title更新成功");
    }

    @Override
    public ResponseResult getSeoInfo() {
        TbSettings description = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_DESCRIPTION);
        TbSettings webKeyWords = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_KEYWORDS);
        Map<String,String> result = new HashMap<>();
        result.put(description.getKey(),description.getValue());
        result.put(webKeyWords.getKey(),webKeyWords.getValue());
        return ResponseResult.success("获取seo信息成功").setData(result);
    }

    @Override
    public ResponseResult putSeoInfo(String keywords, String description) {
        //判断
        if (TextUtils.isEmpty(keywords)) {
            return ResponseResult.failed("关键不能为空");
        }
        if (TextUtils.isEmpty(description)) {
            return ResponseResult.failed("描述不能为空");
        }
        TbSettings descriptions = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_DESCRIPTION);
        TbSettings webKeyWords = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_KEYWORDS);
        if (descriptions==null){
            descriptions = new TbSettings();
            descriptions.setId(idWorker.nextId()+"");
            descriptions.setKey(Constrants.Settings.WEB_SIZE_DESCRIPTION);
            descriptions.setCreateTime(new Date());
            descriptions.setUpdateTime(new Date());
            descriptions.setValue(description);
            settingsMapper.insertinto(descriptions);
        }

        if (webKeyWords==null){
            webKeyWords = new TbSettings();
            webKeyWords.setId(idWorker.nextId()+"");
            webKeyWords.setKey(Constrants.Settings.WEB_SIZE_KEYWORDS);
            webKeyWords.setCreateTime(new Date());
            webKeyWords.setUpdateTime(new Date());
            webKeyWords.setValue(keywords);
            settingsMapper.insertinto(webKeyWords);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("keyWordsValue",keywords);
        map.put("Wordskey",Constrants.Settings.WEB_SIZE_KEYWORDS);
        map.put("DescValue",description);
        map.put("DescKey",Constrants.Settings.WEB_SIZE_DESCRIPTION);
        settingsMapper.updateKeyWords(map);
        settingsMapper.updateDesc(map);

        return ResponseResult.success("更新seo信息成功");
    }

    /*
    * 整个网站的访问量
    * */
    @Override
    public ResponseResult getSizeViewCount() {
        //先从redis拿出来,
        Object viewCountStr = redisUtil.get(Constrants.Settings.WEB_SIZE_VIEW_COUNT);
        TbSettings webTitle = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_VIEW_COUNT);
        if (webTitle==null) {
            webTitle = new TbSettings();
            webTitle.setId(idWorker.nextId()+"");
            webTitle.setKey(Constrants.Settings.WEB_SIZE_VIEW_COUNT);
            webTitle.setUpdateTime(new Date());
            webTitle.setCreateTime(new Date());
            webTitle.setValue("1");
            settingsMapper.insertinto(webTitle);
        }
        //如果没有则从数据库拿，然后更新到redis
        if (viewCountStr==null){
            viewCountStr = webTitle.getValue();
            redisUtil.set(Constrants.Settings.WEB_SIZE_VIEW_COUNT,viewCountStr);
        }else {
            //如果redis有值则更新到数据库
//            webTitle.setValue(viewCountStr+"");
            settingsMapper.updateSettingsById(viewCountStr+"",Constrants.Settings.WEB_SIZE_VIEW_COUNT);
//            settingsMapper.updateSettings(webTitle);
        }

        TbSettings webTitle1 = settingsMapper.getWebTitle(Constrants.Settings.WEB_SIZE_VIEW_COUNT);

        Map<String,Integer> result = new HashMap<>();
        result.put(webTitle1.getKey(),Integer.valueOf(webTitle1.getValue()));
        return ResponseResult.success("获取网站信息浏览量成功").setData(result);
    }

    @Autowired
    private RedisUtil redisUtil;
    /*
    * 统计访问
    * */
    @Override
    public void updateViewCount() {
        //redis时机
        Object viewCount = redisUtil.get(Constrants.Settings.WEB_SIZE_VIEW_COUNT);
        if (viewCount==null){


            TbSettings settings = settingsMapper.hasKey(Constrants.Settings.WEB_SIZE_VIEW_COUNT);
            redisUtil.set(Constrants.Settings.WEB_SIZE_VIEW_COUNT,Integer.valueOf(settings.getValue()));
        }
        //自增
        redisUtil.incr(Constrants.Settings.WEB_SIZE_VIEW_COUNT,1);














    }
}
