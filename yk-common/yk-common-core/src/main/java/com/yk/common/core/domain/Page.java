package com.yk.common.core.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page<T> implements Serializable {

    public Page() {
        this.pageNo = 1;
        this.pageSize = 20;
        this.list = Collections.emptyList();
    }

    public Page(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Page(int pageNo, int pageSize, int size, long total, int pages) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.size = size;
        this.total = total;
        this.pages = pages;
    }

    /**
     * 页码，从1开始
     */
    private int pageNo;

    /**
     * 页面大小
     */
    private int pageSize;

    /**
     * 当前页的数量
     */
    private int size;

    /**
     * 总数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * list
     */
    private List<T> list;

    public static <T, K> Page<K> reNew(Page<T> oldPage, List<K> newList) {
        Page newPage = new Page<>(oldPage.getPageNo(), oldPage.getPageSize(), oldPage.getSize(), oldPage.getTotal(),
            oldPage.getPages());
        newPage.setList(newList);
        return newPage;
    }

}
