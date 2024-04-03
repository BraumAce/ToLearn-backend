package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新密码请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "更新密码请求")
public class UpdatePasswordRequest {

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;
}
