package com.toLearn.Exception;

import com.toLearn.Common.ErrorCode;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serial;

/**
 * 业务异常处理程序
 * @author BraumAce
 */
public class BusinessException extends RuntimeException {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 4946461703861202476L;

    @ApiModelProperty(value = "业务异常")
    private final int code;

    @ApiModelProperty(value = "描述")
    private final String description;


    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    /**
     * 获取代码
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取描述
     * @return {@link String}
     */
    public String getDescription() {
        return description;
    }

}
