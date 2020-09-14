package com.wwjjbt.sob_blog_system_mp.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbUser对象", description="")
public class TbUser implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色")
    private String roles;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "状态：0表示删除，1表示正常")
    @TableLogic
    private String state;

    @ApiModelProperty(value = "注册ip")
    private String regIp;

    @ApiModelProperty(value = "登录Ip")
    private String loginIp;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

//    private Long total;
//    private Long pages;


}
