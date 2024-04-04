package com.toLearn.Model.Domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 博文评论
 * @author BraumAce
 */
@Data
@TableName(value = "blog_comments")
@ApiModel(value = "博文评论")
public class BlogComments implements Serializable {
    // 主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "博文id")
    private Long blogId;

    @ApiModelProperty(value = "关联的1级评论id")
    private Long parentId;

    @ApiModelProperty(value = "回复的评论id")
    private Long answerId;

    @ApiModelProperty(value = "回复的内容")
    private String content;

    @ApiModelProperty(value = "id")
    private Integer likedNum;

    // 状态，0：正常，1：被举报，2：禁止查看
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
