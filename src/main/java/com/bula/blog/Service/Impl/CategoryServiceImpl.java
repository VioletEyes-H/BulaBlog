package com.bula.blog.Service.Impl;

import com.bula.blog.Entity.Blog;
import com.bula.blog.Entity.Category;
import com.bula.blog.Repository.CategoryRepository;
import com.bula.blog.Service.CategoryService;
import com.bula.blog.Util.BlogUtils;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;
import com.bula.blog.Util.ResultGenerator;
import com.bula.blog.Vo.CountCategory;
import com.bula.blog.Vo.SimpleBlogVo;
import com.bula.blog.Vo.SimpleCategories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Resource
    private CategoryRepository categoryRepository;

    /**
     * 分类页  查询分类列表和文章数量
     *
     * @return
     */
    @Override
    @Cacheable(value = "categories", key = "'category_count'", cacheManager = "redisCacheManager")
    public List<CountCategory> getCategoriesCount() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Iterable<Category> categories = categoryRepository.findAll(sort);
        List<CountCategory> countCategories = new ArrayList<>();
        if (null != categories) {
            for (Category category : categories) {
                CountCategory countCategory = new CountCategory();
                BeanUtils.copyProperties(category, countCategory);
                countCategory.setCount(category.getBlogs().size());
                countCategories.add(countCategory);
                logger.info("cccc:" + new BlogUtils<Blog>().BlogVOToString(category.getBlogs()));
            }
        }
        return countCategories;
    }

    /**
     * 分类  通过分类查询所有文章
     *
     * @param categoryName
     * @return
     */
    @Override
    @Cacheable(value = "categories", key = "'category_blogs_'+#categoryName", cacheManager = "redisCacheManager")
    public List<Blog> getBlogForCategory(String categoryName) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Category category = categoryRepository.findByCategoryName(categoryName, sort);
        List<Blog> blogs = category.getBlogs();
        Collections.sort(blogs, (o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        return blogs;
    }

    /**
     * 获取所有分类总数
     *
     * @return
     */
    @Override
    public int getCategoryCount() {
        return (int) categoryRepository.count();
    }

    /**
     * 获取所有分类
     *
     * @return
     */
    @Override
    public List<Category> getCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        return (List<Category>) categories;
    }

    /**
     * 通过分类Id查询分类
     *
     * @param id
     * @return
     */
    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    /**
     * 根据分类名称查询分类
     *
     * @param name
     * @return
     */
    @Override
    public List<Category> getCategoryByNameLike(String name, Sort sort) {
        return categoryRepository.findByCategoryNameLike("%" + name + "%", sort);
    }

    @Override
    public PageResult getCategoryForPage(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);//查询状态为1的博客并进行分页
        List<SimpleCategories> simpleCategoriesList = new ArrayList<>();
        for (Category category : categories.getContent()) {
            SimpleCategories simpleCategories = new SimpleCategories();
            BeanUtils.copyProperties(category, simpleCategories);
            simpleCategories.setId(category.getId() + "");
            simpleCategoriesList.add(simpleCategories);
        }
        PageResult pageResult = new PageResult();
        pageResult.setTotalPage(categories.getTotalPages());//查询总页数
        pageResult.setTotalCount(categories.getNumberOfElements());//查询总记录数
        pageResult.setCurrPage(categories.getNumber() + 1);//查询当前页数
        pageResult.setPageSize(limit);//每页总记录数
        pageResult.setList(simpleCategoriesList);//数据源
        return pageResult;
    }

    @Override
    @Transactional
    public Result updateCategory(SimpleCategories simpleCategories) {
        Category category = categoryRepository.findById(Integer.parseInt(simpleCategories.getId()));
        if (null == category) {
            return ResultGenerator.genFailResult("数据不存在");
        }
        category.setCategoryIcon(simpleCategories.getCategoryIcon());
        category.setCategoryName(simpleCategories.getCategoryName());
        return ResultGenerator.genSuccessResult("修改成功");
    }

    @Override
    @Transactional
    public Result saveCategory(SimpleCategories simpleCategories) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Category category = categoryRepository.findByCategoryName(simpleCategories.getCategoryName(), sort);
        if (null != category) {
            return ResultGenerator.genFailResult("分类已存在");
        }
        Category category1 = new Category();
        category1.setCategoryName(simpleCategories.getCategoryName());
        category1.setCategoryIcon(simpleCategories.getCategoryIcon());
        Category s = categoryRepository.save(category1);
        if (null == s) {
            return ResultGenerator.genFailResult("添加失败");
        }
        return ResultGenerator.genSuccessResult("添加成功");
    }

    @Override
    @Transactional
    public Result deleteCategory(Integer[] ids) {
        for (int id : ids) {
            Category category = categoryRepository.findById(id);
            if (null == category) {
                return ResultGenerator.genFailResult("分类：" + category.getCategoryName() + "数据不存在");
            }
            if (category.getBlogs().size() > 0) {
                return ResultGenerator.genFailResult("分类：" + category.getCategoryName() + " 删除失败,该分类下有文章");
            }
            categoryRepository.deleteById(id);
        }
        return ResultGenerator.genSuccessResult("删除成功");
    }
}
