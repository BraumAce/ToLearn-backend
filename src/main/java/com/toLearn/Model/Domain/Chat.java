package com.toLearn.Model.Domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 聊天
 * @author BraumAce
 */
@Data
@TableName(value = "chat")
@ApiModel(value = "聊天")
public class Chat implements Serializable {
    // 聊天记录id
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "发送消息id")
    private Long fromId;

    @ApiModelProperty(value = "接收消息id")
    private Long toId;

    @ApiModelProperty(value = "正文")
    private String text;

    // 聊天类型 1-私聊 2-群聊
    @ApiModelProperty(value = "聊天类型")
    private Integer chatType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "队伍id")
    private Long teamId;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
