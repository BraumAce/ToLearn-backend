package com.toLearn.Model.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户vo
 * @author BraumAce
 */
@Data
@ApiModel(value = "用户返回")
public class UserVO implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 642307645491206784L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String username;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "标签")
    private String tags;

    // 状态 0 - 正常
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String profile;

    @ApiModelProperty(value = "用户角色")
    private Integer role;

    @ApiModelProperty(value = "是否关注")
    private Boolean isFollow;
}
