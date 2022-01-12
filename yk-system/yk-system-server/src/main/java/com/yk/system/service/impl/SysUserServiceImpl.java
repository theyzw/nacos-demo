package com.yk.system.service.impl;

import com.yk.common.mybatis.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.yk.system.dto.SysUserDto;
import com.yk.system.dao.SysUserDao;
import com.yk.system.entity.SysUserEntity;
import com.yk.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDto, SysUserDao, SysUserEntity>
    implements SysUserService {



}