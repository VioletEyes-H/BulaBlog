package com.bula.blog.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_blog")
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tittle;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @CreationTimestamp //由数据库自动创建时间
    private Date createTime;
    private String content;
    private int status;//博客状态
    private int reading;//阅读量
    private int praise;//点赞量

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;//分类

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "tb_blog_and_tag",
            joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<BlogTag> tagList;//标签


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Blog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<BlogTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<BlogTag> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", tittle='" + tittle + '\'' +
                ", createTime=" + createTime +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", reading=" + reading +
                ", praise=" + praise +
                ", category=" + category +
                ", tagList=" + tagList +
                '}';
    }
}
