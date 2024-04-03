package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 队伍退出请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "队伍退出请求")
public class TeamKickOutRequest {

    @ApiModelProperty(value = "队伍id")
    private Long teamId;

    @ApiModelProperty(value = "用户id")
    private Long userId;
}
