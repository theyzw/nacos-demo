package com.yk.common.log.aspect;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author yzw
 * @date 2022/01/15 11:58
 */
@Slf4j
//@Aspect
//@Component
public class WebLogAspect {

    private static final List<String> HEAD_NOT_INCLUDE =
        Arrays.asList("Accept", "Accept-Encoding", "Accept-Charset",
            "Accept-Language", "Connection",
            "Content-Encoding", "Content-Type", "Vary",
            "Cache-Control", "Cookie", "Host", "accept",
            "accept-encoding", "accept-charset",
            "accept-language", "connection",
            "content-encoding", "content-type", "vary",
            "cache-control", "cookie", "host",
            "Content-Length", "SLB-IP", "User-Agent",
            "Sec-Fetch-Dest", "Sec-Fetch-Mode",
            "Sec-Fetch-User", "Postman-Token",
            "X-Forwarded-Proto", "Sec-Fetch-Site", "SLB-ID",
            "sec-ch-ua", "X-Tag");

    /** 以 controller 包下定义的所有请求为切入点 */
//    @Pointcut("execution(public * com.carbonstop.*.controller..*.*(..))")
    @Pointcut("execution(* com.carbonstop..*.controller.*.*(..))")
    public void webLog() {
    }

    /**
     * 在切点之前织入
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 打印请求相关参数
        log.info("==== start ====");
        log.info("url          : {}", request.getRequestURL().toString());
        log.info("http-method  : {}", request.getMethod());
        log.info("content-type : {}", request.getContentType());
        // 打印调用 controller 的全路径以及执行方法
        log.info("class-method : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName());
        log.info("ip           : {}", request.getRemoteAddr());

//        Enumeration<String> e = request.getHeaderNames();
//        while (e.hasMoreElements()) {
//            String headName = e.nextElement();
//            if (!HEAD_NOT_INCLUDE.contains(headName)) {
//                log.info("header       : {}:{}", headName, request.getHeader(headName));
//            }
//        }
//
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length > 0) {
//            for (Cookie c : cookies) {
//                log.info("cookie       : {}:{}", c.getName(), c.getValue());
//            }
//        }

//        log.info("request      : {}", JSON.toJSONString(joinPoint.getArgs()));
//        log.info("request      : {}", new Gson().toJson(joinPoint.getArgs()));
    }

    /**
     * 在切点之后织入
     *
     * @throws Throwable
     */
    @After("webLog()")
    public void doAfter() throws Throwable {
        log.info("==== end ====");
        // 每个请求之间空一行
        log.info("");
    }

    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
//        log.info("response  : {}", new Gson().toJson(result));
//        log.info("response  : {}", JSON.toJSONString(result));
        // 执行耗时
        log.info("cost-time : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }
}
