package com.bula.blog.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class SimpleBlogVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String tittle;
    private String summary;//简介，概述
    private int reading;//阅读量
    private String category;
    //    private int praise;//点赞
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private int status;
    private String tag;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

//    public int getPraise() {
//        return praise;
//    }

//    public void setPraise(int praise) {
//        this.praise = praise;
//    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleBlogVo{" +
                "id=" + id +
                ", tittle='" + tittle + '\'' +
                ", summary='" + summary + '\'' +
                ", reading=" + reading +
                ", category='" + category + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", tag='" + tag + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
