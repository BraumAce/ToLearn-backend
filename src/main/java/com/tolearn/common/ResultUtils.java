package com.tolearn.common;

/**
 * 返回工具类
 * @author BraumAce
 */
public class ResultUtils {
    private ResultUtils(){}

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     *
     * @param code
     * @param message
     * @param description
     * @return
     * @param <T>
     */
    public static<T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     * @param <T>
     */
    public static<T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse<String> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), description);
    }
}
