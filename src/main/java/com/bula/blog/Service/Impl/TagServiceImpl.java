package com.bula.blog.Service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.bula.blog.Entity.Blog;
import com.bula.blog.Entity.BlogTag;
import com.bula.blog.Entity.Category;
import com.bula.blog.Repository.BlogTagRepository;
import com.bula.blog.Service.TagService;
import com.bula.blog.Util.*;
import com.bula.blog.Vo.CountTagVo;
import com.bula.blog.Vo.SimpleBlogVo;
import com.bula.blog.Vo.SimpleCategories;
import com.bula.blog.Vo.SimpleTags;
import com.mysql.cj.xdevapi.JsonArray;
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
import javax.swing.text.html.parser.TagElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private BlogTagRepository blogTagRepository;

    /**
     * 首页 侧边栏 最热标签
     *
     * @return
     */
    @Override
    @Cacheable(value = "indexTag", key = "'index_side_tag'", cacheManager = "redisCacheManager")
    public List<CountTagVo> getHotTagIndexForSide() {
        Iterable<BlogTag> blogTags = blogTagRepository.findAll();
        List<CountTagVo> countTagVos = new ArrayList<>();
        for (BlogTag blogTag : blogTags) {
            int count = blogTag.getBlogs().size();
            if (count == 0) {
                continue;//筛选掉没有文章的标签
            }
            CountTagVo countTagVo = new CountTagVo();
            countTagVo.setTagName(blogTag.getTagName());
            countTagVo.setId(blogTag.getId());
            countTagVo.setCount(count);
            countTagVos.add(countTagVo);
        }
        Collections.sort(countTagVos);
        if (countTagVos.size() > 20) {
            return countTagVos.subList(0, 20);//返回最火的20个标签
        } else {
            return countTagVos;
        }
    }

    /**
     * 通过标签获取文章
     *
     * @param tagName
     * @param pageNum
     * @return
     */
    @Override
    public PageResult getTagIndexForBlogs(String tagName, int pageNum) {
        BlogTag blogTag = blogTagRepository.findByTagName(tagName);
        List<Blog> blogs = blogTag.getBlogs();
        Collections.sort(blogs, (o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));//根据时间倒序排序
        List<SimpleBlogVo> simpleBlogVos = new ArrayList<>();
        for (Blog blog : blogs) {
            simpleBlogVos.add(BlogUtils.CopyAndSubForSimpleBlog(blog));
        }
        PageUtils pageUtils = new PageUtils(pageNum, 20);
        return pageUtils.page(simpleBlogVos);
    }

    /**
     * 获取标签总数
     *
     * @return
     */
    @Override
    public int getTagCount() {
        return (int) blogTagRepository.count();
    }

    /**
     * 通过标签名字列表查询每一个标签类
     *
     * @param name
     * @return
     */
    @Override
    @Cacheable(value = "tags", key = "#name", cacheManager = "redisCacheManager")
    public List<BlogTag> getBlogTagByName(List<String> name) {
        List<BlogTag> blogTags = new ArrayList<>();
        for (String str : name) {
            BlogTag blogTag = blogTagRepository.findByTagName(str);
            if (null == blogTag) {//如果查不到就创建一个
                BlogTag b = new BlogTag();
                b.setTagName(str);
                blogTags.add(this.saveBlogTag(b));
            }
            blogTags.add(blogTag);
        }
        return blogTags;
    }

    /**
     * 保存标签
     *
     * @param blogTag
     * @return
     */
    @Override
    public BlogTag saveBlogTag(BlogTag blogTag) {
        return blogTagRepository.save(blogTag);
    }

    /**
     *
     * 通过标签名保存标签
     * @param name
     * @return
     */
    @Override
    public Result saveBlogTagByName(String name) {
        BlogTag blogTag = blogTagRepository.findByTagName(name);
        if (null != blogTag) {
            return ResultGenerator.genFailResult("添加失败，标签已存在");
        }
        blogTag = new BlogTag();
        blogTag.setTagName(name);
        blogTag = this.saveBlogTag(blogTag);
        if(null==blogTag){
            return ResultGenerator.genFailResult("添加失败");
        }else
        {
            return ResultGenerator.genSuccessResult("添加成功");
        }
    }

    @Override
    public PageResult getTagsForPage(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<BlogTag> blogTags = blogTagRepository.findAll(pageable);
        List<SimpleTags> simpleTagsList = new ArrayList<>();
        for (BlogTag blogTag : blogTags.getContent()) {
            SimpleTags simpleTags = new SimpleTags();
            BeanUtils.copyProperties(blogTag, simpleTags);
            simpleTagsList.add(simpleTags);
        }
        PageResult pageResult = new PageResult();
        pageResult.setTotalPage(blogTags.getTotalPages());//查询总页数
        pageResult.setTotalCount(blogTags.getNumberOfElements());//查询总记录数
        pageResult.setCurrPage(blogTags.getNumber() + 1);//查询当前页数
        pageResult.setPageSize(limit);//每页总记录数
        pageResult.setList(simpleTagsList);//数据源
        return pageResult;
    }

    @Override
    public Result delete(Integer[] ids) {
        for (int id : ids) {
            BlogTag blogTag = blogTagRepository.findById(id);
            if (blogTag.getBlogs().size() > 0) {
                return ResultGenerator.genFailResult("标签：" + blogTag.getBlogs() + " 删除失败,该分类下有文章");
            }
            blogTagRepository.deleteById(id);
        }
        return ResultGenerator.genSuccessResult("删除成功");
    }
}
