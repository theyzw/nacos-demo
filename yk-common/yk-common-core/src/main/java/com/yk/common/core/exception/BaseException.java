package com.yk.common.core.exception;

/**
 * 作为所有系统异常的公共类
 * 自定义异常继承Exception，是受检异常。
 *
 * @author yzw
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

}
