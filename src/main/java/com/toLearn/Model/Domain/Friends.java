package com.toLearn.Model.Domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 好友
 * @author BraumAce
 */
@TableName(value = "friends")
@Data
@ApiModel(value = "好友")
public class Friends implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "发送申请的用户id")
    private Long fromId;

    @ApiModelProperty(value = "接收申请的用户id")
    private Long receiveId;

    @ApiModelProperty(value = "是否已读")
    private Integer isRead;

    @ApiModelProperty(value = "申请状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

    @ApiModelProperty(value = "好友申请备注信息")
    private String remark;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
