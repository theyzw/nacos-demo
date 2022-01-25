package com.yk.security.service;

import cn.hutool.core.lang.UUID;
import com.google.common.collect.Maps;
import com.yk.common.core.consts.CacheConsts;
import com.yk.common.core.consts.SecurityConsts;
import com.yk.common.core.utils.IpUtils;
import com.yk.common.core.utils.JwtUtils;
import com.yk.common.core.utils.ServletUtils;
import com.yk.common.core.utils.StringUtils;
import com.yk.common.redis.utils.RedisUtil;
import com.yk.common.core.model.LoginUser;
import com.yk.security.utils.SecurityUtils;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConsts.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConsts.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConsts.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = UUID.fastUUID().toString();

        loginUser.setToken(token);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = Maps.newHashMap();
        claimsMap.put(SecurityConsts.USER_KEY, token);
        claimsMap.put(SecurityConsts.DETAILS_USER_ID, loginUser.getUserid());
        claimsMap.put(SecurityConsts.DETAILS_USERNAME, loginUser.getUsername());

        // 接口返回信息
        Map<String, Object> rspMap = Maps.newHashMap();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = RedisUtil.get(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            RedisUtil.del(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());

        RedisUtil.set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}