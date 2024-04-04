package com.toLearn.Model.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 队伍vo
 * @author BraumAce
 */
@Data
@ApiModel(value = "队伍返回")
public class TeamVO implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 6986365414601034543L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "队伍名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "封面图片")
    private String coverImage;

    @ApiModelProperty(value = "最大人数")
    private Integer maxNum;

    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

    @ApiModelProperty(value = "队长id")
    private Long userId;

    // 状态 0 - 公开，1 - 私有，2 - 加密
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private UserVO createUser;

    private String leaderName;

    @ApiModelProperty(value = "已加入的用户数")
    private Long hasJoinNum;

    @ApiModelProperty(value = "是否已加入队伍")
    private boolean hasJoin = false;

    private List<String> joinedUserAvatars;
}
