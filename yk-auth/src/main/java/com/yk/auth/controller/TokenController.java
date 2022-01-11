package com.yk.auth.controller;

import com.yk.auth.request.LoginReq;
import com.yk.auth.response.TokenResp;
import com.yk.auth.service.SysLoginService;
import com.yk.common.core.domain.ApiResult;
import com.yk.security.model.LoginUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * token
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@Validated
@Api(tags = "token")
public class TokenController {
//    @Autowired
//    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public ApiResult<TokenResp> login(@RequestBody LoginReq req) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(req.getUsername(), req.getPassword());
        // 获取登录token
        // 接口返回信息
        TokenResp resp = TokenResp.builder().accessToken("adsfd").expiresIn(111L).build();

        return ApiResult.ok(resp);
//        return R.ok(tokenService.createToken(userInfo));
    }

//    @DeleteMapping("logout")
//    public R<?> logout(HttpServletRequest request)
//    {
//        String token = SecurityUtils.getToken(request);
//        if (StringUtils.isNotEmpty(token))
//        {
//            String username = JwtUtils.getUserName(token);
//            // 删除用户缓存记录
//            AuthUtil.logoutByToken(token);
//            // 记录用户退出日志
//            sysLoginService.logout(username);
//        }
//        return R.ok();
//    }
//
//    @PostMapping("refresh")
//    public R<?> refresh(HttpServletRequest request)
//    {
//        LoginUser loginUser = tokenService.getLoginUser(request);
//        if (StringUtils.isNotNull(loginUser))
//        {
//            // 刷新令牌有效期
//            tokenService.refreshToken(loginUser);
//            return R.ok();
//        }
//        return R.ok();
//    }
//
//    @PostMapping("register")
//    public R<?> register(@RequestBody RegisterBody registerBody)
//    {
//        // 用户注册
//        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
//        return R.ok();
//    }
}
