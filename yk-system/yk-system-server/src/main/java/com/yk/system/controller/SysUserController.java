package com.yk.system.controller;

import com.yk.common.core.domain.ApiResult;
import com.yk.common.core.domain.Page;
import com.yk.system.dto.SysUserDto;
import com.yk.system.service.SysUserService;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户
 */
@Slf4j
@RestController
@Validated
@Api(tags = "系统用户")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("page")
    public ApiResult<Page<SysUserDto>> page(@NotNull(message = "pageNo不能为空") Integer pageNo,
                                            @NotNull(message = "pageSize不能为空") Integer pageSize) {
        Page<SysUserDto> page = sysUserService.findPage(null, pageNo, pageSize);

        return ApiResult.ok(page);
    }

    @PostMapping("create")
    public ApiResult create(@RequestBody @Valid SysUserDto req) {

        log.info("req={}", req);

        return ApiResult.ok();
    }

}
