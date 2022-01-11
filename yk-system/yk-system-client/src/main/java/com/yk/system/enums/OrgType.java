package com.yk.system.enums;

import com.google.common.collect.Maps;
import com.yk.common.core.enums.BaseEnumInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * 组织类型
 *
 * @author yzw
 * @date 2019/07/18 11:45
 */
// jackson的序列化及反序列化方法
//@JsonDeserialize(using = BaseEnumInterfaceJsonDeserializer.class)
//@JsonSerialize(using = BaseEnumInterfaceJsonSerializer.class)
public enum OrgType implements BaseEnumInterface {

    ONE(1, "sdf"),
    TWO(2, "sdfdf");

    private static Map<Integer, OrgType> VALUE_MAP = Maps.newHashMap();

    static {
        for (OrgType p : OrgType.values()) {
            VALUE_MAP.put(p.getCode(), p);
        }
        VALUE_MAP = Collections.unmodifiableMap(VALUE_MAP);
    }

    private int code;
    private String name;

    OrgType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrgType get(Integer value) {
        return VALUE_MAP.get(value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCode() {
        return code;
    }


}