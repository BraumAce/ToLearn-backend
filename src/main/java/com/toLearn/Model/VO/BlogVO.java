package com.toLearn.Model.VO;

import com.toLearn.Model.Domain.Blog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 博客vo
 * @author BraumAce
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "博文返回")
public class BlogVO extends Blog implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = -1461567317259590205L;

    @ApiModelProperty(value = "是否点赞")
    private Boolean isLike;

    @ApiModelProperty(value = "封面图片")
    private String coverImage;

    @ApiModelProperty(value = "作者")
    private UserVO author;
}
