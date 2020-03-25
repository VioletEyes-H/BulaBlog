package com.bula.blog.Repository;

import com.bula.blog.Entity.Blog;
import com.bula.blog.Entity.BlogTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BlogTagRepository extends PagingAndSortingRepository<BlogTag, Integer> {
    BlogTag findByTagName(String tagName);

    BlogTag findById(int id);
}
