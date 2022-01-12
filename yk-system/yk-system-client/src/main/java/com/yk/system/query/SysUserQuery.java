package com.yk.system.query;

import com.yk.common.core.enums.EnableStatus;
import java.util.Date;
import com.yk.common.mybatis.query.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserQuery implements Query {

    private Long id;
    private String userid;
    private String mobile;
    private EnableStatus userStatus;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
}