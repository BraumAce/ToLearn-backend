package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "消息请求")
public class MessageRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 1324635911327892058L;

    @ApiModelProperty(value = "接收id")
    private Long toId;

    @ApiModelProperty(value = "队伍id")
    private Long teamId;

    @ApiModelProperty(value = "文本")
    private String text;

    @ApiModelProperty(value = "聊天类型")
    private Integer chatType;

    @ApiModelProperty(value = "是否为管理员")
    private boolean isAdmin;
}
