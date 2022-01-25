package com.yk.common.core.utils.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点树
 *
 * @author yzw
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tree implements Serializable {

    private int code;

    private String name;

    private int pcode;

    @JsonIgnore
    private int sortId;

    /**
     * 不返回空数组
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tree> children;

}
