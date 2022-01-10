package com.yk.security.feign;

import com.yk.common.core.consts.SecurityConsts;
import com.yk.common.core.utils.IpUtils;
import com.yk.common.core.utils.ServletUtils;
import com.yk.common.core.utils.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * feign 请求拦截器
 *
 * @author ruoyi
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (StringUtils.isNotNull(httpServletRequest)) {
            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
            // 传递用户信息请求头，防止丢失
            String userId = headers.get(SecurityConsts.DETAILS_USER_ID);
            if (StringUtils.isNotEmpty(userId)) {
                requestTemplate.header(SecurityConsts.DETAILS_USER_ID, userId);
            }
            String userName = headers.get(SecurityConsts.DETAILS_USERNAME);
            if (StringUtils.isNotEmpty(userName)) {
                requestTemplate.header(SecurityConsts.DETAILS_USERNAME, userName);
            }
            String authentication = headers.get(SecurityConsts.AUTHORIZATION_HEADER);
            if (StringUtils.isNotEmpty(authentication)) {
                requestTemplate.header(SecurityConsts.AUTHORIZATION_HEADER, authentication);
            }

            // 配置客户端IP
            requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
        }
    }
}