package com.yk.common.core.exception;

import com.yk.common.core.code.BaseCodeInterface;
import lombok.Getter;

@Getter
public class ServiceException extends BaseException {

    private Integer code;

    public ServiceException() {

    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(BaseCodeInterface code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public ServiceException(BaseCodeInterface code) {
        super(code.getMessage());
        this.code = code.getCode();
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
