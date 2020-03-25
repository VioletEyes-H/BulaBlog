package com.bula.blog.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_link")
public class Link implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int linkId;
    private String linkName;
    private String linkUrl;
    private String linkDescription;//网站描述
    private int linkRank;//网站排序
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @CreationTimestamp //由数据库自动创建时间
    private Date createTime;
    private Byte linkType;

    public Byte getLinkType() {
        return linkType;
    }

    public void setLinkType(Byte linkType) {
        this.linkType = linkType;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    public int getLinkRank() {
        return linkRank;
    }

    public void setLinkRank(int linkRank) {
        this.linkRank = linkRank;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Link{" +
                "linkId=" + linkId +
                ", linkName='" + linkName + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", linkDescription='" + linkDescription + '\'' +
                ", linkRank=" + linkRank +
                ", createTime=" + createTime +
                ", linkType=" + linkType +
                '}';
    }
}
