package com.toLearn.Model.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.toLearn.Model.Domain.User;

import java.io.Serial;
import java.io.Serializable;

/**
 * 好友记录vo
 * @author BraumAce
 */
@Data
@ApiModel(value = "好友记录返回")
public class FriendsRecordVO implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 1928465648232335L;

    @ApiModelProperty(value = "id")
    private Long id;

    // 申请状态 默认0 （0-未通过 1-已同意 2-已过期）
    @ApiModelProperty(value = "申请状态")
    private Integer status;

    @ApiModelProperty(value = "好友申请备注信息")
    private String remark;

    @ApiModelProperty(value = "申请用户")
    private User applyUser;
}
