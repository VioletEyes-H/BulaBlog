package com.bula.blog.Service;

import com.bula.blog.Entity.Link;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;

import java.util.List;
import java.util.Map;

public interface LinkService {
    Map<Byte, List<Link>> getAllLinks();

    PageResult getLinksList(int page, int limit);

    Result save(Link link);

    Result update(Link link);

    Result findById(int id);

    Result delete(Integer[] ids);
}
