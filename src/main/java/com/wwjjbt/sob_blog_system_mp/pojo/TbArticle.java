package com.wwjjbt.sob_blog_system_mp.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

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
 * @since 2020-06-21
 */

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbArticle对象", description="")
public class TbArticle implements Serializable {

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

    @ApiModelProperty(value = "文章内容")
    private String content;

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

    //设置一个集合将标签给分离出来
    @TableField(exist = false)
    private List<String> ArticleLabels = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<String> getArticleLabels() {
        return ArticleLabels;
    }

    public void setArticleLabels(List<String> articleLabels) {
        ArticleLabels = articleLabels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLabels() {
        this.ArticleLabels.clear();
        //打散到集合中去,如果不包含 - 直接添加，包含则分割成数组，再转成集合
        if (!this.labels.contains("-")) {
            this.ArticleLabels.add(this.labels);
        }else {
            String[] split = this.labels.split("-");
            List<String> strings = Arrays.asList(split);
            this.ArticleLabels.addAll(strings);
        }
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}
