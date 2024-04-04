package com.toLearn.Model.VO;

import com.toLearn.Model.Domain.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 消息vo
 * @author BraumAce
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "消息返回")
public class MessageVO extends Message {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 4353136955942044222L;

    @ApiModelProperty(value = "发送用户id")
    private UserVO fromUser;

    @ApiModelProperty(value = "博客")
    private BlogVO blog;

    @ApiModelProperty(value = "评论")
    private BlogCommentsVO comment;
}
