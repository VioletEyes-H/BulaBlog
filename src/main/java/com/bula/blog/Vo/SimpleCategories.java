package com.bula.blog.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class SimpleCategories implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String categoryName;
    private String categoryIcon;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    @Override
    public String toString() {
        return "SimpleCategories{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", categoryIcon='" + categoryIcon + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
