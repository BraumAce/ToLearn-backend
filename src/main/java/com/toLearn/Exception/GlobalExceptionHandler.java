package com.toLearn.Exception;

import com.toLearn.Common.BaseResponse;
import com.toLearn.Common.ErrorCode;
import com.toLearn.Common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理程序
 * @author BraumAce
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常处理程序
     * @param e e
     * @return {@link BaseResponse}
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 运行时异常处理程序
     * @param e e
     * @return {@link BaseResponse}
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<T> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
