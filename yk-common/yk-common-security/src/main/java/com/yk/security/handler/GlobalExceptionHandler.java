package com.yk.security.handler;

import com.yk.common.core.code.ApiCode;
import com.yk.common.core.consts.HttpStatus;
import com.yk.common.core.domain.ApiResult;
import com.yk.common.core.exception.ServiceException;
import com.yk.common.core.exception.auth.NotPermissionException;
import com.yk.common.core.exception.auth.NotRoleException;
import com.yk.common.core.utils.StringUtils;
import com.yk.common.core.utils.collection.MapBuilder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String TRUE_STR = "true";

    @Value("${api.debug.enable:false}")
    private String debugEnable;

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public ApiResult handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败'{}'", requestURI, e.getMessage());
        return ApiResult.error(HttpStatus.FORBIDDEN, "没有访问权限，请联系管理员授权");
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public ApiResult handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',角色权限校验失败'{}'", requestURI, e.getMessage());
        return ApiResult.error(HttpStatus.FORBIDDEN, "没有访问权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                         HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return ApiResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public ApiResult handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? ApiResult.error(code, e.getMessage()) : ApiResult.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public ApiResult handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ApiResult.error(message);
    }

    /**
     * 参数校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    public ApiResult exception(ConstraintViolationException e) {
        StringBuilder msg = new StringBuilder();
        Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator();
        while (it.hasNext()) {
            msg.append(it.next().getMessage());
            if (it.hasNext()) {
                msg.append(", ");
            }
        }

        log.error("ConstraintViolationException", e);
        return ApiResult.error(ApiCode.REQUEST_PARAM_ERROR, msg.toString());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
//        String message = e.getBindingResult().getFieldError().getDefaultMessage();
//        return ApiResult.error(message);
        StringBuilder msg = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        Iterator<ObjectError> it = bindingResult.getAllErrors().iterator();
        while (it.hasNext()) {
            msg.append(it.next().getDefaultMessage());
            if (it.hasNext()) {
                msg.append(", ");
            }
        }

        return ApiResult.error(ApiCode.REQUEST_PARAM_ERROR, msg.toString());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return ApiResult.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);

        Map<String, Object> exceptionInfo = getExceptionInfo(e);
        return ApiResult.error(ApiCode.INTERNAL_SERVER_ERROR, exceptionInfo);
    }

    private Map<String, Object> getExceptionInfo(Exception e) {
        Map<String, Object> exceptionInfo;
        // 没有明文开启开关，不返回debug信息
        if (!TRUE_STR.equalsIgnoreCase(debugEnable)) {
            exceptionInfo = null;
        } else {
            exceptionInfo = MapBuilder.<String, Object>builder().put("exception", e.toString()).put("trace",
                Arrays.stream(e.getStackTrace()).map(trace -> trace.toString()).collect(Collectors.toList())).build();
        }
        return exceptionInfo;
    }
}
