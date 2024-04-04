package com.toLearn.Model.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聊天信息vo
 * @author BraumAce
 */
@Data
@ApiModel(value = "聊天消息返回")
public class ChatMessageVO implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = -4722378360550337925L;

    @ApiModelProperty(value = "发送id")
    private WebSocketVO fromUser;

    @ApiModelProperty(value = "接收id")
    private WebSocketVO toUser;

    @ApiModelProperty(value = "队伍id")
    private Long teamId;

    @ApiModelProperty(value = "正文")
    private String text;

    @ApiModelProperty(value = "是否是我的消息")
    private Boolean isMy = false;

    @ApiModelProperty(value = "聊天类型")
    private Integer chatType;

    @ApiModelProperty(value = "是否为管理员")
    private Boolean isAdmin = false;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}

