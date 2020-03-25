package com.bula.blog.Controller;

import com.bula.blog.Entity.Link;
import com.bula.blog.Service.LinkService;
import com.bula.blog.Util.Result;
import com.bula.blog.Util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/links")
public class LinkController implements Serializable {
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(LinkController.class);
    @Autowired
    private LinkService linkService;

    @RequestMapping
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "link");
        return "admin/link";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        logger.debug("/links/list请求数据：" + params);
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常");
        }
        int page = Integer.parseInt(params.get("page").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        return ResultGenerator.genSuccessResult(linkService.getLinksList(page, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(Link link) {
        logger.debug("/save友链请求：" + link.toString());
        return linkService.save(link);
    }

    @PostMapping("/update")
    @ResponseBody
    public Result update(Link link) {
        logger.debug("/update友链请求：" + link.toString());
        return linkService.update(link);
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        return linkService.delete(ids);
    }

    @GetMapping("/info/{id}")
    @ResponseBody
    public Result info(HttpServletRequest request, @PathVariable("id") int id) {

        return linkService.findById(id);
    }
}
