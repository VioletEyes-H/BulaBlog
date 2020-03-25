package com.bula.blog.Vo;

import java.io.Serializable;

public class CountTagVo implements Serializable, Comparable<CountTagVo> {
    private static final long serialVersionUID = 1L;
    private int id;
    private String tagName;
    private int Count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public int compareTo(CountTagVo o) {
        int i = o.getCount() - this.getCount();//先按照标签数量降序排序
        if (i == 0) {
            return this.getId() - o.getId();//如果数量相等再用标签id升序排序
        }
        return i;
    }
}
