package com.yk.common.core.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("IdReq")
public class IdReq implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long id;

}