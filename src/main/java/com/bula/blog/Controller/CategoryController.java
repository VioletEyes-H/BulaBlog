package com.bula.blog.Controller;

import com.bula.blog.Entity.Category;
import com.bula.blog.Service.CategoryService;
import com.bula.blog.Util.BlogUtils;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;
import com.bula.blog.Util.ResultGenerator;
import com.bula.blog.Vo.SimpleCategories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @RequestMapping
    public String manager(HttpServletRequest request) {
        request.setAttribute("path", "categories");
        return "admin/category";
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
        PageResult pageResult = categoryService.getCategoryForPage(page, limit);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    @PostMapping("/update")
    @ResponseBody
    public Result update(SimpleCategories simpleCategories) {
        logger.debug("/categories/update请求数据：" + simpleCategories.toString());
        return categoryService.updateCategory(simpleCategories);
    }

    @GetMapping("/save")
    @ResponseBody
    public Result save(SimpleCategories simpleCategories) {
        logger.debug("/categories/update请求数据：" + simpleCategories.toString());
        return categoryService.saveCategory(simpleCategories);
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        logger.debug("/categories/delete请求数据：" + Arrays.toString(ids));
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        return categoryService.deleteCategory(ids);
    }


}
