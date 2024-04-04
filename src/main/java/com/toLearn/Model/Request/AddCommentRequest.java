package com.toLearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 添加博文评论请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "添加博文评论请求")
public class AddCommentRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 5733549433004941655L;

    @ApiModelProperty(value = "博文id")
    private Long blogId;

    @ApiModelProperty(value = "评论")
    private String content;

}
