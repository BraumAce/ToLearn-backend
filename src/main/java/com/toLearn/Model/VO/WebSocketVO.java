package com.toLearn.Model.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 网络套接字vo
 * @author BraumAce
 */
@Data
@ApiModel(value = "websocket返回")
public class WebSocketVO implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 4696612253320170315L;

    @ApiModelProperty(value = "id")
    private long id;

    @ApiModelProperty(value = "用户昵称")
    private String username;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;
}
