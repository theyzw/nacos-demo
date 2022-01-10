package com.yk.common.core.exception;

import com.yk.common.core.code.BaseCodeInterface;
import lombok.Getter;

@Getter
public class BizException extends BaseException {

    private Integer code;

    public BizException() {

    }

    public BizException(String message) {
        super(message);
    }

    public BizException(BaseCodeInterface code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public BizException(BaseCodeInterface code) {
        super(code.getMessage());
        this.code = code.getCode();
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

}
