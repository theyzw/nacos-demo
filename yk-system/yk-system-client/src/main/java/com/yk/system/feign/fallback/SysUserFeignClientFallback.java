package com.yk.system.feign.fallback;

import com.yk.common.core.domain.ApiResult;
import com.yk.common.core.domain.Page;
import com.yk.system.dto.SysUserDto;
import com.yk.system.feign.SysUserFeignClient;
import org.springframework.stereotype.Component;

@Component
public class SysUserFeignClientFallback implements SysUserFeignClient {

    @Override
    public ApiResult<Page<SysUserDto>> page(Integer pageNo, Integer pageSize) {
        return ApiResult.error("");
    }
}
