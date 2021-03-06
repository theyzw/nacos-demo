package com.yk.security.aspect;

import com.yk.common.core.consts.SecurityConsts;
import com.yk.common.core.exception.ServiceException;
import com.yk.common.core.utils.ServletUtils;
import com.yk.common.core.utils.StringUtils;
import com.yk.security.annotation.InnerAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 内部服务调用验证处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered {

    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        String source = ServletUtils.getRequest().getHeader(SecurityConsts.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals(SecurityConsts.INNER, source)) {
            throw new ServiceException("没有内部访问权限，不允许访问");
        }

        String userid = ServletUtils.getRequest().getHeader(SecurityConsts.DETAILS_USER_ID);
        String username = ServletUtils.getRequest().getHeader(SecurityConsts.DETAILS_USERNAME);
        // 用户信息验证
        if (innerAuth.isUser() && (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username))) {
            throw new ServiceException("没有设置用户信息，不允许访问 ");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
