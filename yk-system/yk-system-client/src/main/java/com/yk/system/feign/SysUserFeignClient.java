package com.yk.system.feign;

import com.yk.common.core.consts.ServiceNameConsts;
import com.yk.common.core.domain.ApiResult;
import com.yk.common.core.domain.PageResult;
import com.yk.system.dto.SysUserDto;
import com.yk.system.feign.fallback.SysUserFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "sysUserFeignClient", name = ServiceNameConsts.SYSTEM_SERVICE, fallback = SysUserFeignClientFallback.class)
public interface SysUserFeignClient {

    @GetMapping("/user/page")
    ApiResult<PageResult<SysUserDto>> page(@RequestParam("pageNo") Integer pageNo,
                                           @RequestParam("pageSize") Integer pageSize);
}
