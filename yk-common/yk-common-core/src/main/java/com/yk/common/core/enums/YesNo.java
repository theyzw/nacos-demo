package com.yk.common.core.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.yk.common.core.convert.deserializer.BaseEnumInterfaceJsonDeserializer;
import com.yk.common.core.convert.serializer.BaseEnumInterfaceJsonSerializer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * 是否
 *
 * @author yzw
 * @date 2019/07/18 11:45
 */
@JsonDeserialize(using = BaseEnumInterfaceJsonDeserializer.class)
@JsonSerialize(using = BaseEnumInterfaceJsonSerializer.class)
public enum YesNo implements BaseEnumInterface {

    /** 否 */
    NO(0, "否"),
    /** 是 */
    YES(1, "是");

    private static Map<Integer, YesNo> VALUE_MAP = Maps.newHashMap();

    static {
        for (YesNo p : YesNo.values()) {
            VALUE_MAP.put(p.getCode(), p);
        }
        VALUE_MAP = Collections.unmodifiableMap(VALUE_MAP);
    }

    private int code;
    private String name;

    YesNo(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static YesNo get(Integer value) {
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

    public static YesNo reverse(YesNo yesNoStatus) {
        if (yesNoStatus == null) {
            return null;
        }

        return NO.equals(yesNoStatus) ? YES : NO;
    }

    public static YesNo and(YesNo... yesNoStatuses) {
        if (yesNoStatuses.length == 0) {
            return NO;
        }

        return Arrays.stream(yesNoStatuses).filter(NO::equals).findAny().orElse(YES);
    }

}