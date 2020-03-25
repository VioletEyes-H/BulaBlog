package com.bula.blog.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bula.blog.Entity.Blog;
import com.bula.blog.Entity.BlogTag;
import com.bula.blog.Entity.Category;
import com.bula.blog.Entity.Link;
import com.bula.blog.Service.BlogService;
import com.bula.blog.Service.CategoryService;
import com.bula.blog.Service.LinkService;
import com.bula.blog.Service.TagService;
import com.bula.blog.Util.BlogUtils;
import com.bula.blog.Util.MarkDownUtil;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Vo.SimpleBlogVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;

    @GetMapping({"/index", "/", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 首页 分页数据
     *
     * @param request
     * @param pageNum
     * @return
     */
    @GetMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        PageResult pageResult = blogService.getBlogsForIndexPage(pageNum);
        if (pageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", pageResult);
        logger.info("首页数据:" + new BlogUtils<SimpleBlogVo>().BlogVOToString((List<SimpleBlogVo>) pageResult.getList()));
        request.setAttribute("hotBlogs", blogService.getBlogsListFotIndexPage(1));
        request.setAttribute("newBlogs", blogService.getBlogsListFotIndexPage(2));
        request.setAttribute("pageName", "首页");
//        request.setAttribute("giveBlogs", blogService.getBlogsListFotIndexPage(2));
        request.setAttribute("hotTags", tagService.getHotTagIndexForSide());
        return "yummy/index";
    }

    /**
     * 分类页面
     *
     * @return
     */
    @GetMapping("/categories")
    public String categories(HttpServletRequest request) {
        return this.categoriesForIndex(request, null);
    }

    /**
     * 分类页面  分页
     *
     * @return
     */
    @GetMapping("/categories/{categoryName}")
    public String categoriesForIndex(HttpServletRequest request, @PathVariable("categoryName") String categoryName) {
        List<Blog> blogs;
        if (null == categoryName) {
            blogs = blogService.getAllBlogsList();
        } else {
            blogs = categoryService.getBlogForCategory(categoryName);
        }
        logger.info("分类数据：" + new BlogUtils<Blog>().BlogVOToString(blogs));
        if (blogs == null)
            return "erro/erro_404";
        request.setAttribute("categoryBlogs", blogs);
        request.setAttribute("pageName", "分类");
        request.setAttribute("categoryCount", categoryService.getCategoriesCount());
        return "yummy/category";
    }

    @GetMapping("/tag/{tagName}")
    public String tagForTagName(HttpServletRequest request, @PathVariable("tagName") String tagName) {
        return this.tagForTagNameIndex(request, tagName, 1);
    }

    @GetMapping("/tag/{tagName}/{pageNum}")
    public String tagForTagNameIndex(HttpServletRequest request, @PathVariable("tagName") String tagName, @PathVariable("pageNum") int pageNum) {
        request.setAttribute("blogPageResult", tagService.getTagIndexForBlogs(tagName, pageNum));
        request.setAttribute("hotBlogs", blogService.getBlogsListFotIndexPage(1));
        request.setAttribute("newBlogs", blogService.getBlogsListFotIndexPage(2));
        request.setAttribute("hotTags", tagService.getHotTagIndexForSide());
        request.setAttribute("pageName", "标签");
        return "yummy/index";
    }

    @GetMapping("/blog/{id}")
    public String blogDetail(HttpServletRequest request, @PathVariable("id") int id) {
        Blog blog = blogService.getBlogDetail(id);
        blog.setContent(MarkDownUtil.mdToHtml(blog.getContent()));
        request.setAttribute("blogDetailVO", blog);
        request.setAttribute("pageName", blog.getTittle());
        return "yummy/detail";
    }

    @GetMapping("/link")
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "关于");
        Map<Byte, List<Link>> listMap = linkService.getAllLinks();
        if (null != listMap) {
            if (listMap.containsKey((byte) 0)) {
                request.setAttribute("favoriteLinks", listMap.get((byte) 0));
            }
            if (listMap.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", listMap.get((byte) 1));
            }
            if (listMap.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", listMap.get((byte) 2));
            }
        }
        return "yummy/link";
    }
}
