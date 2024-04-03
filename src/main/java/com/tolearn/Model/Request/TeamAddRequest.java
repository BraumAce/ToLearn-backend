package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 队伍添加请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "队伍添加请求")
public class TeamAddRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    @ApiModelProperty(value = "队伍名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "最大人数")
    private Integer maxNum;

    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

    // 状态 0 - 公开，1 - 私有，2 - 加密
    @ApiModelProperty(value = "状态 0 - 公开，1 - 私有，2 - 加密")
    private Integer status;

    @ApiModelProperty(value = "密码")
    private String password;

}
