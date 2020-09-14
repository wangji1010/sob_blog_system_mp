package com.wwjjbt.sob_blog_system_mp.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbArticle对象", description="")
public class TbArticleNoContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "分类ID")
    private String categoryId;


    @ApiModelProperty(value = "类型（0表示富文本，1表示markdown）")
    private String type;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "状态（1表示已发布，2表示草稿，0表示删除 3、置顶）")
    private String state = "1";

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "标签")
    private String labels;

    @ApiModelProperty(value = "阅读数量")
    private Integer viewCount = 0;

    @ApiModelProperty(value = "发布时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
