package com.yk.common.core.domain;

import com.yk.common.core.code.ApiCode;
import com.yk.common.core.consts.HttpStatus;
import com.yk.common.core.enums.EnvType;
import com.yk.common.core.exception.ServiceException;
import com.yk.common.core.utils.collection.MapBuilder;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    /**
     * debug信息(例如异常堆栈)
     */
    private Object debug;

    public ApiResult() {
    }

    public ApiResult(ApiCode apiCode) {
        this(apiCode.getCode(), apiCode.getMessage());
    }

    public ApiResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ApiResult(ApiCode apiCode, T data) {
        this(apiCode.getCode(), apiCode.getMessage());
        this.data = data;
    }

    private ApiResult(ApiCode apiCode, T data, Object debug) {
        this(apiCode.getCode(), apiCode.getMessage());
        this.data = data;
        this.debug = debug;
    }

    public static ApiResult ok() {
        return new ApiResult(ApiCode.SUCCESS);
    }

    public static <T> ApiResult<T> ok(T value) {
        return new ApiResult<>(ApiCode.SUCCESS, value);
    }

    public static ApiResult ok(String name, Object value) {
        return new ApiResult<>(ApiCode.SUCCESS, MapBuilder.<String, Object>builder().put(name, value).build());
    }

    public static ApiResult ok(Map<String, Object> data) {
        return new ApiResult<>(ApiCode.SUCCESS, data);
    }

    public static ApiResult error(ServiceException exception) {
        return new ApiResult(exception.getCode() == null ? 500 : exception.getCode(), exception.getMessage());
    }

    public static ApiResult error(ApiCode code, String msg) {
        return new ApiResult(code.getCode(), msg);
    }

    public static ApiResult error(int code, String msg) {
        return new ApiResult(code, msg);
    }

    public static ApiResult error(ApiCode code) {
        return new ApiResult(code);
    }

    public static <T> ApiResult<T> error(ApiCode code, T value) {
        return new ApiResult<>(code, value);
    }

    public static ApiResult error(ApiCode code, String name, Object value) {
        return new ApiResult<>(code, MapBuilder.builder().put(name, value).build());
    }

    public static ApiResult error(ApiCode code, Map<String, Object> debug) {
        return new ApiResult<>(code, null, debug);
    }

    public static ApiResult error(String msg) {
        return new ApiResult(HttpStatus.ERROR, msg);
    }

    public static ApiResult error(ApiCode code, Map<String, Object> debug, String env) {
        if (EnvType.PROD.getActive().equals(env)) {
            return new ApiResult<>(code, null, null);
        }
        return new ApiResult<>(code, null, debug);
    }

}
