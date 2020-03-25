package com.bula.blog.Util;

import java.util.List;

public class PageUtils {
    private int pageno;//当前页
    private int pageSize;//每页展示多少数据
    private int totalPage = 0;//总页数

    public PageUtils(int pageno, int pageSize) {
        this.pageno = pageno;
        this.pageSize = pageSize;
    }

    public PageResult page(List<?> list) {
        PageResult page = new PageResult();
        //分页
        Integer totalNum = list.size();//记录总数
        if (totalNum == 0) {
            return null;
        }
        //默认从零分页，这里要考虑这种情况，下面要计算。
        int pageNum = pageno;
        if (totalNum > 0) {
            totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        }
        if (pageNum > totalPage) {
            pageNum = totalPage;
        }
        int startPoint = (pageNum - 1) * pageSize;
        int endPoint = startPoint + pageSize;
        if (totalNum <= endPoint) {
            endPoint = totalNum;
        }
        list = list.subList(startPoint, endPoint);
        page.setCurrPage(pageno);
        page.setTotalCount(totalNum);
        page.setTotalPage(totalPage);
        page.setPageSize(pageSize);
        page.setList(list);
        return page;
    }
}
