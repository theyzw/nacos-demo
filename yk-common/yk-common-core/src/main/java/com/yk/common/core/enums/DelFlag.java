package com.yk.common.core.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.yk.common.core.convert.deserializer.BaseEnumInterfaceJsonDeserializer;
import com.yk.common.core.convert.serializer.BaseEnumInterfaceJsonSerializer;
import java.util.Collections;
import java.util.Map;

@JsonDeserialize(using = BaseEnumInterfaceJsonDeserializer.class)
@JsonSerialize(using = BaseEnumInterfaceJsonSerializer.class)
public enum DelFlag implements BaseEnumInterface {

    NORMAL(0, "正常"),
    DELETED(1, "已删除"),

    ;

    private static Map<Integer, DelFlag> VALUE_MAP = Maps.newHashMap();

    static {
        for (DelFlag element : DelFlag.values()) {
            VALUE_MAP.put(element.getCode(), element);
        }
        VALUE_MAP = Collections.unmodifiableMap(VALUE_MAP);
    }

    private int code;
    private String name;

    DelFlag(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DelFlag get(Integer value) {
        return VALUE_MAP.get(value);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

}
