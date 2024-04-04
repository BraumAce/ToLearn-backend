package com.toLearn.Model.Domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 标签
 * @author BraumAce
 */
@Data
@TableName(value = "tag")
@ApiModel(value = "标签")
public class Tag implements Serializable {
    //主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "上传该标签的用户id")
    private Long userId;

    @ApiModelProperty(value = "父标签id")
    private Long parentId;

    // 是否为父标签 0-不是父标签，1-父标签
    @ApiModelProperty(value = "是否为父标签 0-不是父标签，1-父标签")
    private Integer isParent;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
