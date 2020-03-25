package com.bula.blog.Controller;

import com.bula.blog.Entity.Blog;
import com.bula.blog.Service.BlogService;
import com.bula.blog.Service.CategoryService;
import com.bula.blog.Util.*;
import com.bula.blog.Vo.ResultBlogtVo;
import com.bula.blog.Vo.SimpleBlogVo;
import com.mysql.cj.PreparedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/admin/blogs")
public class BlogController {
    @Autowired
    private RedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(BlogController.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BlogService blogService;

    /**
     * 文章管理
     *
     * @param request
     * @return
     */
    @RequestMapping
    public String manage(HttpServletRequest request) {
        request.setAttribute("path", "blog");
        return "admin/blog";
    }

    /**
     * 编写文章
     *
     * @param request
     * @return
     */
    @GetMapping("/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("categories", categoryService.getCategories());
        return "admin/edit";
    }

    /**
     * 保存文章
     *
     * @param resultBlogtVo
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result save(ResultBlogtVo resultBlogtVo) {
        logger.debug("/save文章保存请求：" + resultBlogtVo.toString());
        return blogService.saveBlog(resultBlogtVo);
    }

    /**
     * 请求文章列表
     *
     * @param params
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        logger.debug("/blog/list请求数据：" + params);
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常");
        }
        int page = Integer.parseInt(params.get("page").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        if (!StringUtils.isEmpty(params.get("keyword"))) {
            String key = params.get("keyword").toString();
            return ResultGenerator.genSuccessResult(blogService.getBlogLikeNameForPage(page, limit, key));
        }
        PageResult pageResult = blogService.getBlogForManager(page, limit);
        logger.debug("/blog/list返回数据" + pageResult.getList());
        return ResultGenerator.genSuccessResult(pageResult);
    }

    /**
     * 修改文章页面
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/edit/{id}")
    public String editForId(@PathVariable("id") int id, HttpServletRequest request) {
        request.setAttribute("path", "edit");
        Blog blog = blogService.getBlogDetail(id);
        SimpleBlogVo simpleBlogVo = BlogUtils.CopyForSimpleBlog(blog);
        if (null == blog) {
            return "/error/error_400";
        }
        request.setAttribute("blog", simpleBlogVo);
        logger.debug("/edit/{id}响应的数据：" + simpleBlogVo.toString());
        request.setAttribute("categories", categoryService.getCategories());
        return "admin/edit";
    }

    @PostMapping("/update")
    @ResponseBody
    public Result update(ResultBlogtVo resultBlogtVo) {
        logger.debug("文章修改请求：" + resultBlogtVo.toString());
        return blogService.updateBlog(resultBlogtVo);
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        return blogService.deleteBlog(ids);
    }


}
