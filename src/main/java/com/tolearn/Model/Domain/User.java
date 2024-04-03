package com.tolearn.Model.Domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @author BraumAce
 */
@Data
@TableName(value = "user")
@ApiModel(value = "用户")
public class User implements Serializable {
    // 主键
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String username;

    @ApiModelProperty(value = "账号")
    private String userAccount;

    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;

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

    // 用户状态，0为正常
    @ApiModelProperty(value = "用户状态，0为正常")
    private Integer status;

    @ApiModelProperty(value = "好友id")
    private String friendIds;

    /// 用户角色 0-普通用户,1-管理员
    @ApiModelProperty(value = "用户角色 0-普通用户,1-管理员")
    private Integer role;

    @ApiModelProperty(value = "标签列表")
    private String tags;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
