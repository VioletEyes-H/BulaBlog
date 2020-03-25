package com.bula.blog.Repository;

import com.bula.blog.Entity.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    Category findByCategoryName(String categoryName, Sort sort);

    Category findById(int id);

    List<Category> findByCategoryNameLike(String categoryName, Sort sort);

}
