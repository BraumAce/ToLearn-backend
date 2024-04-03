package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "用户注册请求")
public class UserRegisterRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户密码")
    private String userPassword;

    @ApiModelProperty(value = "校验密码")
    private String checkPassword;

}
