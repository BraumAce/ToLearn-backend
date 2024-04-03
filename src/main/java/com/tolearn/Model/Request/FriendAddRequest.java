package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 好友添加请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "好友添加请求")
public class FriendAddRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 1472823745422792988L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "接收申请的用户id")
    private Long receiveId;

    @ApiModelProperty(value = "好友申请备注信息")
    private String remark;
}
