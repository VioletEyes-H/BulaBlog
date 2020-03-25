package com.bula.blog.Controller;

import com.bula.blog.Entity.Users;
import com.bula.blog.Service.UserService;
import com.bula.blog.Util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/profile")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping
    public String manger() {
        return "/admin/profile";
    }

    @PostMapping("/update")
    @ResponseBody
    public Result updatePassword(HttpServletRequest request, String originalPassword, String newPassword) {
        Users users = (Users) request.getSession().getAttribute("user");
        return userService.updatePassword(originalPassword, newPassword, users.getUsername());
    }

}
