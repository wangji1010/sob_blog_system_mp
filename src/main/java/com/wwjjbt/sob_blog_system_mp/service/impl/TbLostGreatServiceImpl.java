package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostGreat;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostGreatMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostGreatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-10-18
 */
@Service
public class TbLostGreatServiceImpl extends ServiceImpl<TbLostGreatMapper, TbLostGreat> implements TbLostGreatService {

    @Autowired
    private TbLostGreatMapper lostGreatMapper;

    @Autowired
    private TbUserService userService;

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult selectPoint(String a_id) {

        //检查是否登录
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        TbUser tbUser = userService.checkUser(request, response);
        if (tbUser==null){
            return ResponseResult.ACCOUNT_NOT_LOGIN("请先登录账号");
        }
        //根据当前用户的id 和当前文章的id 进行查看是否有点赞
        String u_id = tbUser.getId();
        TbLostGreat tbLostGreat = lostGreatMapper.selectGreatBya_u(a_id, u_id);
        if (tbLostGreat==null){
            //点赞
            TbLostGreat great = new TbLostGreat();
            great.setId(idWorker.nextId()+"");
            great.setUId(u_id);
            great.setPId(a_id);
            great.setCreateTime(new Date());
            great.setDateTime(new Date());

            lostGreatMapper.insert(great);
            return ResponseResult.success("点赞成功");

        }else {
            //取消点赞 , 直接删除数据
            String id = tbLostGreat.getId();
            lostGreatMapper.deleteGreatPoint(id);
            return ResponseResult.success("取消点赞");

        }

    }

    /*
    * 查询当前文章的点赞数量
    * */
    @Override
    public ResponseResult selectCount(String a_id) {
        QueryWrapper<TbLostGreat> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id",a_id);
        Integer count = lostGreatMapper.selectCount(wrapper);
        if (count==null){
            count=0;
        }
        return ResponseResult.success("查询成功").setData(count);
    }
}
