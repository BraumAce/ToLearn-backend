package com.toLearn.Model.VO;

import com.toLearn.Model.Domain.BlogComments;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 博客评论vo
 * @author BraumAce
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "博文评论返回")
public class BlogCommentsVO extends BlogComments implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 5695588849785352130L;

    @ApiModelProperty(value = "评论用户")
    private UserVO commentUser;

    @ApiModelProperty(value = "是否点赞")
    private Boolean isLiked;

    @ApiModelProperty(value = "博客")
    private BlogVO blog;
}
