package com.yk.common.core.code;

import java.io.Serializable;

/**
 * 枚举常见的返回码
 *
 * @author yzw
 */
public enum ApiCode implements BaseCodeInterface, Serializable {

    SUCCESS(200, "OK"),
    FAIL(500, "fail"),

    REQUEST_PARAM_ERROR(1000, "传入参数错误"),
    REQUEST_PARAM_MISSING(1001, "缺少参数"),
    REQUEST_TOKEN_PARAM_MISSING(1002, "缺少登录参数"),
    COOKIE_ERROR(1100, "cookie错误"),
    BIZ_EXCEPTION(1200, "业务异常"),
    INTERNAL_SERVER_ERROR(1300, "服务器错误"),
    SERVICE_UNAVAILABLE(1400, "操作失败，请重试"),
    TOKEN_ERROR(1500, "token校验失败"),
    SIGN_ERROR(1600, "签名校验失败"),
    USER_ERROR(1700, "用户校验失败"),
    USER_NOT_FOUND(1800, "用户不存在"),

    BAD_REQUEST(5000, "请求错误"),
    UNAUTHORIZED(5001, "未认证"),
    FORBIDDEN(5002, "没有权限访问"),
    NOT_FOUND(5003, "请求资源不存在"),
    TOO_MANY_REQUESTS(5004, "请求次数超过限制"),
    THIRD_SERVER_ERROR(5006, "第三方服务器错误"),
    INVALID_REQUEST(5007, "不支持的请求方式"),

    ;

    private Integer code;
    private String message;

    ApiCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}