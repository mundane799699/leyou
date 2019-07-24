package com.leyou.search.pojo;

import java.util.Map;

public class SearchRequest {
    private String key; // 搜索字段
    private Integer page; // 当前页
    private static final int DEFAULT_SIZE = 20;
    private static final int DEFAULT_PAGE = 1;
    private Map<String, String> filter;

    public Map<String, String> getFilter() {
        return filter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPage() {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getSize() {
        return DEFAULT_SIZE;
    }
}
