package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 队伍加入请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "队伍加入请求")
public class TeamJoinRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = -3755024144750907374L;

    @ApiModelProperty(value = "队伍id", required = true)
    private Long teamId;

    @ApiModelProperty(value = "密码")
    private String password;

}
