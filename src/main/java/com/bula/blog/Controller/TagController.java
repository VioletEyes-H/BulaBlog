package com.bula.blog.Controller;

import com.bula.blog.Entity.BlogTag;
import com.bula.blog.Service.TagService;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;
import com.bula.blog.Util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/tags")
public class TagController {
    private Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    @RequestMapping
    public String manager(HttpServletRequest request) {
        request.setAttribute("path", "tags");
        return "admin/tag";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        logger.debug("/categories/list请求数据：" + params);
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常");
        }
        int page = Integer.parseInt(params.get("page").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        PageResult pageResult = tagService.getTagsForPage(page, limit);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(String tagName) {
        return tagService.saveBlogTagByName(tagName);
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        return tagService.delete(ids);
    }
}
