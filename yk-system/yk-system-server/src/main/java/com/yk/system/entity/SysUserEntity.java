package com.yk.system.entity;

import com.yk.common.core.enums.EnableStatus;
import java.util.Date;
import com.yk.common.mybatis.entity.BaseEntity;
import com.yk.system.dto.SysUserDto;
import lombok.Data;

@Data
public class SysUserEntity extends BaseEntity<SysUserDto> {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户状态。0 正常 1 禁用
     */
    private EnableStatus userStatus;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public SysUserEntity() {
    }

    public SysUserEntity(SysUserDto dto) {
        super(dto);
    }

}
