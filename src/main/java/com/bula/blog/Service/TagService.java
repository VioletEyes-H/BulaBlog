package com.bula.blog.Service;

import com.bula.blog.Entity.BlogTag;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;
import com.bula.blog.Vo.CountTagVo;

import java.util.List;

public interface TagService {
    List<CountTagVo> getHotTagIndexForSide();

    PageResult getTagIndexForBlogs(String tagName, int pageNum);

    int getTagCount();

    List<BlogTag> getBlogTagByName(List<String> name);

    BlogTag saveBlogTag(BlogTag blogTag);

    PageResult getTagsForPage(int page, int limit);

    Result delete(Integer[] ids);

    Result saveBlogTagByName(String name);
}
