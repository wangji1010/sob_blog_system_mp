package com.wwjjbt.sob_blog_system_mp.mapper;

import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {


    @Update(" UPDATE `tb_user` SET `state`='0' WHERE `id`=#{userId} AND `state`='1'")
    void deleteLogicById(String userId);

    @Update("update `tb_user` set `password` = #{password} where `email` = #{email}")
    void updatePasswordByEmail(String password,String email);

    @Update("update `tb_user` set `email`=#{email} where `id`=#{id}")
    void updateEmailById(String email, String id);
}
