package com.bula.blog.Controller;

import com.bula.blog.Entity.Users;
import com.bula.blog.Service.BlogService;
import com.bula.blog.Service.CategoryService;
import com.bula.blog.Service.TagService;
import com.bula.blog.Service.UserService;
import com.bula.blog.Vo.SimpleCountVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/login-error")
    public String loginError(HttpServletRequest request) {
        AuthenticationException exceptionMessage = (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");//获取Security的异常
        request.setAttribute("loginError", true);
        request.setAttribute("errorMsg", exceptionMessage.getMessage());
        return "admin/login";
    }

    @GetMapping({"/index", "/"})
    public String index(HttpServletRequest request) {
        logger.debug("登录成功");
        request.setAttribute("path", "index");
        SimpleCountVo simpleCountVo = new SimpleCountVo();
        simpleCountVo.setBlogCount(blogService.getBlogCount());
        simpleCountVo.setTagCount(tagService.getTagCount());
        simpleCountVo.setCategoryCount(categoryService.getCategoryCount());
        request.setAttribute("count", simpleCountVo);
        return "admin/index";
    }


}
