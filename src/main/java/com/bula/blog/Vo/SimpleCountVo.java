package com.bula.blog.Vo;

import java.io.Serializable;

public class SimpleCountVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int blogCount;
    private int tagCount;
    private int categoryCount;

    public int getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(int blogCount) {
        this.blogCount = blogCount;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }
}
