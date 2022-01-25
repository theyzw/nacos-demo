package com.yk.calc.service.impl;

import com.yk.calc.service.CalcService;
import com.yk.common.core.domain.ApiResult;
import com.yk.common.core.domain.PageResult;
import com.yk.system.dto.SysUserDto;
import com.yk.system.feign.SysUserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yzw
 * @date 2022/01/12 15:22
 */
@Slf4j
@Service
public class CalcServiceImpl implements CalcService {

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

    @Override
    public void test(int pageNo, int pageSize) {
        ApiResult<PageResult<SysUserDto>> page = sysUserFeignClient.page(pageNo, pageSize);
        log.info("{}", page);
    }
}
