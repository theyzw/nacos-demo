package com.yk.system.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.yk.system.BaseTest;
import com.yk.system.dto.SysUserDto;
import com.yk.system.query.SysUserQuery;
import com.yk.system.service.SysUserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SysUserServiceImplTest extends BaseTest {

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void test() {
        List<SysUserDto> list = sysUserService.findList(SysUserQuery.builder().createBy("a").build());
        print(list);
    }
}