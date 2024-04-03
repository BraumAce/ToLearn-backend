package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 删除请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "删除请求")
public class DeleteRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = -7428525903309954640L;

    @ApiModelProperty(value = "id")
    private long id;
}
