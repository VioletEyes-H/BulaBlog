package com.bula.blog.Service;

import com.bula.blog.Entity.Blog;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;
import com.bula.blog.Vo.ResultBlogtVo;
import com.bula.blog.Vo.SimpleBlogVo;

import java.util.List;

public interface BlogService {
    PageResult getBlogsForIndexPage(int page);

    List<SimpleBlogVo> getBlogsListFotIndexPage(int type);

    List<Blog> getAllBlogsList();

    Blog getBlogDetail(int id);

    void addReading(int id);

    int getBlogCount();

    Result saveBlog(ResultBlogtVo resultBlogtVo);

    Result updateBlog(ResultBlogtVo resultBlogtVo);

    Result deleteBlog(Integer[] ids);

    PageResult getBlogForManager(int page, int limit);

    PageResult getBlogLikeNameForPage(int page, int limit, String key);
}
