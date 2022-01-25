package com.yk.common.core.domain;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    public PageResult() {
        this.pageNo = 1;
        this.pageSize = 20;
        this.list = Collections.emptyList();
    }

    public PageResult(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageResult(int pageNo, int pageSize, int size, long total, int pages) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.size = size;
        this.total = total;
        this.pages = pages;
    }

    public PageResult(Page page) {
        this.pageNo = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.size = page.size();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.list = page.getResult();
    }

    /**
     * 页码，从1开始
     */
    @ApiModelProperty(value = "页码，从1开始")
    private int pageNo;

    /**
     * 页面大小
     */
    @ApiModelProperty(value = "页面大小")
    private int pageSize;

    /**
     * 当前页的数量
     */
    @ApiModelProperty(value = "当前页的数量")
    private int size;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private long total;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private int pages;

    /**
     * list
     */
    @ApiModelProperty(value = "list")
    private List<T> list;

    public static <T, K> PageResult<K> reNew(PageResult<T> oldPage, List<K> newList) {
        PageResult newPage = new PageResult<>(oldPage.getPageNo(), oldPage.getPageSize(), oldPage.getSize(), oldPage.getTotal(),
            oldPage.getPages());
        newPage.setList(newList);
        return newPage;
    }

}
