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
public enum EnableStatus implements BaseEnumInterface {

    ON(0, "正常"),
    OFF(1, "禁用"),

    ;

    private static Map<Integer, EnableStatus> VALUE_MAP = Maps.newHashMap();

    static {
        for (EnableStatus element : EnableStatus.values()) {
            VALUE_MAP.put(element.getCode(), element);
        }
        VALUE_MAP = Collections.unmodifiableMap(VALUE_MAP);
    }

    private int code;
    private String name;

    EnableStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EnableStatus get(Integer value) {
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
