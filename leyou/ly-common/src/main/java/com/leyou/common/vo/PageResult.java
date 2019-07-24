package com.leyou.common.vo;

import java.util.List;
import lombok.Data;

// view object

@Data
public class PageResult<T> {
    private Long total;
    private Integer totalPage;
    private List<T> items;

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
