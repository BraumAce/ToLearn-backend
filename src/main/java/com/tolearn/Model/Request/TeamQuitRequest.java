package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 队伍退出请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "队伍退出请求")
public class TeamQuitRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 1473299551300760408L;

    @ApiModelProperty(value = "队伍id")
    private Long teamId;

}
