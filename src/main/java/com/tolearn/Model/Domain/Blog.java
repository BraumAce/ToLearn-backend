package com.tolearn.Model.Domain;

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
 * 博客实体类
 * @author BraumAce
 */
@Data
@TableName(value = "blog")
@ApiModel(value = "博文")
public class Blog implements Serializable {
    // 主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图片")
    private String images;

    @ApiModelProperty(value = "正文")
    private String content;

    @ApiModelProperty(value = "点赞数量")
    private Integer likedNum;

    @ApiModelProperty(value = "评论数量")
    private Integer commentsNum;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
