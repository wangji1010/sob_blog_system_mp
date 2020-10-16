package com.wwjjbt.sob_blog_system_mp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangji
 * @since 2020-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbLostArticle对象", description="")
public class TbLostArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "描述")
    private String summary;

    @ApiModelProperty(value = "0 全部 1丢失 2拾到")
    private Integer type;

    @ApiModelProperty(value = "失物内容")
    private String content;

    @ApiModelProperty(value = "失物图片")
    private String cover;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "状态（1表示已发布，2表示草稿，3表示删除）")
    private String state;

    @ApiModelProperty(value = "浏览量")
    private Integer viewCount;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
