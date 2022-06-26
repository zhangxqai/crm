package com.bjpowernode.crm.vo;

import java.util.List;

public class ClueVo<T> {
    private int total;
    private List<T> ClueList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getClueList() {
        return ClueList;
    }

    public void setClueList(List<T> clueList) {
        ClueList = clueList;
    }
}
