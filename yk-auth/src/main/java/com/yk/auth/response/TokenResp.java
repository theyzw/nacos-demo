package com.yk.auth.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @date 2022/01/11 15:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("登录token")
public class TokenResp implements Serializable {

    @ApiModelProperty(value = "accessToken")
    private String accessToken;

    @ApiModelProperty(value = "过期时间")
    private Long expiresIn;
}
