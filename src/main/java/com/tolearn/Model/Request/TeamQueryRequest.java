package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 队队搜索请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "队伍搜索请求")
public class TeamQueryRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 9111600376030432964L;

    @ApiModelProperty(value = "队伍id")
    private Long id;

    @ApiModelProperty(value = "id列表")
    private List<Long> idList;

    // 搜索关键词（同时对队伍名称和描述搜索）
    @ApiModelProperty(value = "搜索关键词")
    private String searchText;

    @ApiModelProperty(value = "队伍名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "最大人数")
    private Integer maxNum;

    // 队长id
    @ApiModelProperty(value = "用户id")
    private Long userId;

    // 状态 0 - 公开，1 - 私有，2 - 加密
    @ApiModelProperty(value = "状态 0 - 公开，1 - 私有，2 - 加密")
    private Integer status;

}
