package com.bula.blog.Service.Impl;

import com.bula.blog.Entity.Blog;
import com.bula.blog.Entity.BlogTag;
import com.bula.blog.Entity.Category;
import com.bula.blog.Repository.BlogRepository;
import com.bula.blog.Service.BlogService;
import com.bula.blog.Service.CategoryService;
import com.bula.blog.Service.TagService;
import com.bula.blog.Util.*;
import com.bula.blog.Vo.ResultBlogtVo;
import com.bula.blog.Vo.SimpleBlogVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);
    @Resource
    private BlogRepository blogRepository;

    @Resource
    private CategoryService categoryService;

    @Resource
    private TagService tagService;

    /**
     * 首页 分页
     *
     * @param page 页数
     * @return
     */
    @Override
    @Cacheable(value = "index", key = "'index_page_'+#page", cacheManager = "redisCacheManager")
    public PageResult getBlogsForIndexPage(int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<Blog> blogPage = blogRepository.findByStatus(1, pageable);//查询状态为1的博客并进行分页
        List<SimpleBlogVo> simpleBlogVos = new ArrayList<>();
        for (Blog blog : blogPage.getContent()) {
            simpleBlogVos.add(BlogUtils.CopyAndSubForSimpleBlog(blog));
        }
        Collections.sort(simpleBlogVos, (o1, o2) -> o2.getReading() - o1.getReading());//根据阅读量倒序排序
        PageResult pageResult = new PageResult();
        pageResult.setTotalPage(blogPage.getTotalPages());//查询总页数
        pageResult.setTotalCount(blogPage.getNumberOfElements());//查询总记录数
        pageResult.setCurrPage(blogPage.getNumber() + 1);//查询当前页数
        pageResult.setPageSize(10);//每页总记录数
        pageResult.setList(simpleBlogVos);//数据源
        return pageResult;
    }

    /**
     * 首页 侧边栏
     *
     * @param type 1：点击最多  2：最新文章
     * @return
     */
    @Override
    @Cacheable(value = "index", key = "'index_page_side_'+#type", cacheManager = "redisCacheManager")
    public List<SimpleBlogVo> getBlogsListFotIndexPage(int type) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<SimpleBlogVo> simpleBlogVos = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 20, sort);//根据时间降序查询前20条
        Page<Blog> blogPage = blogRepository.findByStatus(1, pageable);
        List<Blog> blogList = blogPage.getContent();
        if (!CollectionUtils.isEmpty(blogList)) {
            for (Blog blog : blogList) {
                SimpleBlogVo simpleBlogListVO = new SimpleBlogVo();
                BeanUtils.copyProperties(blog, simpleBlogListVO);
                simpleBlogVos.add(simpleBlogListVO);
            }
        }
        //把最近发布的20条博客按照热度进行排序
//        Collections.sort(simpleBlogVos);
        if (type == 1) {
            Collections.sort(simpleBlogVos, (o1, o2) -> o2.getReading() - o1.getReading());//根据阅读量倒序排序
        }
        if (simpleBlogVos.size() > 6) {
            //然后再返回热度的前6条
            return simpleBlogVos.subList(0, 6);
        } else {
            return simpleBlogVos;
        }
    }

    /**
     * 获取所有博客列表
     *
     * @return
     */
    @Override
    @Cacheable(value = "index", key = "'category_all_blogs'", cacheManager = "redisCacheManager")
    public List<Blog> getAllBlogsList() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return (List<Blog>) blogRepository.findAll(sort);
    }

    /**
     * 详情页
     *
     * @param id
     * @return
     */
    @Override
    public Blog getBlogDetail(int id) {
        return blogRepository.findById(id);
    }

    /**
     * 阅读量+1
     *
     * @param id
     */
    @Override
    @Transactional
    public void addReading(int id) {
        Blog blog = blogRepository.findById(id);
        blog.setReading(blog.getReading() + 1);
    }


    /**
     * 获取博客数量
     *
     * @return
     */
    @Override
    public int getBlogCount() {
        return (int) blogRepository.count();
    }

    /**
     * 保存博客文章
     *
     * @param resultBlogtVo
     * @return
     */
    @Override
    @Transactional
    public Result saveBlog(ResultBlogtVo resultBlogtVo) {
        Blog blog = new Blog();
        blog.setTittle(resultBlogtVo.getBlogTitle());
        blog.setReading(0);
        blog.setPraise(0);
        blog.setContent(resultBlogtVo.getBlogContent());
        blog.setStatus(resultBlogtVo.getBlogStatus());
        Category category = categoryService.getCategoryById(resultBlogtVo.getBlogCategoryId());
        if (null == category) {
            return ResultGenerator.genFailResult("分类不存在");
        }
        blog.setCategory(category);
        List<String> tagNames = Arrays.asList(resultBlogtVo.getBlogTags().split(","));
        blog.setTagList(tagService.getBlogTagByName(tagNames));
        Blog s = blogRepository.save(blog);//保存到数据库
        if (null == s) {
            return ResultGenerator.genFailResult("保存失败");
        }
//        BlogUtils.refreshRedis("index_page_");
        return ResultGenerator.genSuccessResult("保存成功");
    }

    @Override
    @Transactional
    public Result updateBlog(ResultBlogtVo resultBlogtVo) {
        Blog blog = blogRepository.findById(resultBlogtVo.getBlogId());
        if (null == blog) {
            return ResultGenerator.genFailResult("数据不存在");
        }
        blog.setTittle(resultBlogtVo.getBlogTitle());
        blog.setStatus(resultBlogtVo.getBlogStatus());
        blog.setContent(resultBlogtVo.getBlogContent());
        Category category = categoryService.getCategoryById(resultBlogtVo.getBlogCategoryId());
        blog.setCategory(category);
        List<String> tagNames = Arrays.asList(resultBlogtVo.getBlogTags().split(","));
        blog.setTagList(tagService.getBlogTagByName(tagNames));
        return ResultGenerator.genSuccessResult("更新成功");
    }

    @Override
    @Transactional
    public Result deleteBlog(Integer[] ids) {
        for (int i : ids) {
            blogRepository.deleteById(i);
        }
        return ResultGenerator.genSuccessResult("删除成功");
    }

    /**
     * 管理页查询所有博客并进行分页
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult getBlogForManager(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Blog> pages = blogRepository.findAll(pageable);
        List<SimpleBlogVo> simpleBlogVos = new ArrayList<>();
        for (Blog blog : pages.getContent()) {
            simpleBlogVos.add(BlogUtils.CopyAndSubForSimpleBlog(blog));
        }
        PageResult pageResult = new PageResult();
        pageResult.setTotalPage(pages.getTotalPages());//查询总页数
        pageResult.setTotalCount(pages.getNumberOfElements());//查询总记录数
        pageResult.setCurrPage(pages.getNumber() + 1);//查询当前页数
        pageResult.setPageSize(limit);//每页总记录数
        pageResult.setList(simpleBlogVos);//数据源
        return pageResult;
    }

    /**
     * 通过关键字查找博客标题并进行分页
     *
     * @param page
     * @param limit
     * @param key
     * @return
     */
    @Override
    public PageResult getBlogLikeNameForPage(int page, int limit, String key) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<Blog> blogs = blogRepository.findByTittleLike("%" + key + "%", sort);
        logger.debug("blogs:" + blogs.size());
        List<Blog> allBlog = new ArrayList<>();
        allBlog.addAll(blogs);
        List<Category> category = categoryService.getCategoryByNameLike(key, sort);
        logger.debug("category:" + category
                .size());
        if (null != category) {
            for (Category c : category) {
                allBlog.addAll(c.getBlogs());
            }
        }
        List<SimpleBlogVo> simpleBlogVoList = new ArrayList<>();
        for (Blog blog : allBlog) {
            simpleBlogVoList.add(BlogUtils.CopyForSimpleBlog(blog));
        }
        PageUtils pageUtils = new PageUtils(page, limit);
        return pageUtils.page(simpleBlogVoList);
    }


}
