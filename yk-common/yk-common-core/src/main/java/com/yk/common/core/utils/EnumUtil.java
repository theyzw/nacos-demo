package com.yk.common.core.utils;

import com.yk.common.core.enums.BaseEnumInterface;
import java.util.List;

/**
 * @author ye
 * @date 2018/08/30 23:39
 */
public class EnumUtil {

    /**
     * 根据class和code获取enum值
     *
     * @param enumClass
     * @param code
     * @param <E>
     * @return
     */
    public static <E extends BaseEnumInterface> E codeOf(Class<E> enumClass, Integer code) {
        if (code == null) {
            return null;
        }

        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据枚举名获取枚举，忽略大小写
     *
     * @param enumClass
     * @param code
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> E valueOfIgnoreCase(Class<E> enumClass, String code) {
        return valueOf(enumClass, code, true);
    }

    /**
     * 根据枚举名获取枚举，区分大小写
     *
     * @param enumClass
     * @param code
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> E valueOf(Class<E> enumClass, String code) {
        return valueOf(enumClass, code, false);
    }

    /**
     * 根据枚举名获取枚举
     *
     * @param enumClass
     * @param value
     * @param ignoreCase
     * @param <E>
     * @return
     */
    private static <E extends Enum<E>> E valueOf(Class<E> enumClass, String value, boolean ignoreCase) {
        if (value == null) {
            return null;
        }

        if (ignoreCase) {
            value = value.toUpperCase();
        }

        try {
            E e = Enum.valueOf(enumClass, value);
            return e;
        } catch (Exception e) {
            return null;
        }
    }

    public static <E extends BaseEnumInterface> boolean matchAny(E enumObj, E... code) {
        if (enumObj == null) {
            return false;
        }

        for (E e : code) {
            if (e.equals(enumObj)) {
                return true;
            }
        }

        return false;
    }

    public static <E extends BaseEnumInterface> boolean matchAny(E enumObj, List<E> codeList) {
        if (enumObj == null) {
            return false;
        }

        for (E e : codeList) {
            if (e.equals(enumObj)) {
                return true;
            }
        }

        return false;
    }
}
