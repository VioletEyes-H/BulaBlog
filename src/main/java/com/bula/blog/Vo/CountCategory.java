package com.bula.blog.Vo;

import java.io.Serializable;

public class CountCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String categoryName;
    private int count;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
