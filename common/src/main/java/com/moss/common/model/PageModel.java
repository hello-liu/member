package com.moss.common.model;

import java.util.List;

public class PageModel {

    private Long total;
    private List list;

    public PageModel(Long total, List list) {
        this.total = total;
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
