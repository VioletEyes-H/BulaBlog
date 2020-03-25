package com.bula.blog.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * 处理异常控制器
 */
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public String handleError(HttpServletResponse response) {
        int status = response.getStatus();
        switch (status) {
            case 403:
                return "error/error_400";
            case 500:
                return "error/error_5xx";
            case 404:
                return "error/error_404";
        }
        return "yummy/index";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
