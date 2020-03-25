package com.bula.blog.Repository;

import com.bula.blog.Entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BlogRepository extends PagingAndSortingRepository<Blog, Integer> {

    Blog findById(int id);

    Page<Blog> findByStatus(int state, Pageable pageable);

    List<Blog> findByTittleLike(String tittle, Sort sort);

}
