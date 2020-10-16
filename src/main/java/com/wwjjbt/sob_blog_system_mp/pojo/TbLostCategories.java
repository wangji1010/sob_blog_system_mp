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
@ApiModel(value="TbLostCategories对象", description="")
public class TbLostCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String lName;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "排序")
    private Integer order;

    @ApiModelProperty(value = "状态：0表示不使用，1表示正常")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
