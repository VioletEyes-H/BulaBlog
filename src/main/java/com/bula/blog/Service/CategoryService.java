package com.bula.blog.Service;

import com.bula.blog.Entity.Blog;
import com.bula.blog.Entity.Category;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;
import com.bula.blog.Vo.CountCategory;
import com.bula.blog.Vo.SimpleCategories;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoryService {

    List<CountCategory> getCategoriesCount();

    List<Blog> getBlogForCategory(String categoryName);

    int getCategoryCount();

    List<Category> getCategories();

    Category getCategoryById(int id);

    List<Category> getCategoryByNameLike(String name, Sort sort);

    PageResult getCategoryForPage(int page,int limit);

    Result updateCategory(SimpleCategories simpleCategories);

    Result saveCategory(SimpleCategories simpleCategories);

    Result deleteCategory(Integer[] ids);
}
