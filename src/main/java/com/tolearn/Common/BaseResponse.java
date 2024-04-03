package com.tolearn.Common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 * @author BraumAce
 */
@Data
public class BaseResponse<T> implements Serializable {

    @ApiModelProperty(value = "响应码")
    private int code;


    @ApiModelProperty(value = "数据")
    private T data;


    @ApiModelProperty(value = "消息")
    private String message;


    @ApiModelProperty(value = "描述")
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    /**
     * 返回报错信息
     * @param errorCode 错误代码
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
