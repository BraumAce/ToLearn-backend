package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聊天请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "聊天请求")
public class ChatRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 1445805872513828206L;

    @ApiModelProperty(value = "队伍聊天室id")
    private Long teamId;

    @ApiModelProperty(value = "接收消息id")
    private Long toId;

}
