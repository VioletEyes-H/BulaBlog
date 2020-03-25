package com.bula.blog.Vo;

import java.io.Serializable;

public class ResultBlogtVo implements Serializable {
    private final static long serialVersionUID = 1L;
    private int blogId;
    private String blogTitle;
    private String blogSubUrl;
    private int blogCategoryId;
    private String blogTags;
    private String blogContent;
    private int blogStatus;

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogSubUrl() {
        return blogSubUrl;
    }

    public void setBlogSubUrl(String blogSubUrl) {
        this.blogSubUrl = blogSubUrl;
    }

    public int getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(int blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public String getBlogTags() {
        return blogTags;
    }

    public void setBlogTags(String blogTags) {
        this.blogTags = blogTags;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public int getBlogStatus() {
        return blogStatus;
    }

    public void setBlogStatus(int blogStatus) {
        this.blogStatus = blogStatus;
    }

    @Override
    public String toString() {
        return "ResultBlogtVo{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogSubUrl='" + blogSubUrl + '\'' +
                ", blogCategoryId=" + blogCategoryId +
                ", blogTags='" + blogTags + '\'' +
                ", blogContent='" + blogContent + '\'' +
                ", blogStatus=" + blogStatus +
                '}';
    }
}
