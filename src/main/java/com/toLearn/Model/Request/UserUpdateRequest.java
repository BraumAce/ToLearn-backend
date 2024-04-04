package com.toLearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户更新请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "用户更新请求")
public class UserUpdateRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = -7852848771257290370L;

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String username;

    @ApiModelProperty(value = "个人简介")
    private String profile;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "标签列表")
    private String tags;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "角色")
    private Integer role;
}
