package com.yk.common.core.utils;

import com.github.pagehelper.PageHelper;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 设置请求分页数据
     */
    public static void startPage(int pageNo, int pageSize) {
        pageNo = Math.max(pageNo, 1);
        pageSize = Math.max(pageSize, 1);
        PageHelper.startPage(pageNo, pageSize);
    }

    /**
     * 设置请求分页数据-排序
     */
    public static void startPage(int pageNo, int pageSize, String orderBy) {
        pageNo = Math.max(pageNo, 1);
        pageSize = Math.max(pageSize, 1);
        PageHelper.startPage(pageNo, pageSize).setOrderBy(orderBy);
    }
}
