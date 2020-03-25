package com.bula.blog.Filter;

import com.bula.blog.Entity.Users;
import com.bula.blog.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter implements AuthenticationFailureHandler {
    private Logger logger = LoggerFactory.getLogger(CaptchaFilter.class);
    private String defaultFilterProcessUrl = "/admin/login";
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.equals(request.getServletPath())) {

            try {
                validate(request);
            } catch (AuthenticationException e) {
                this.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    //验证码验证
    private void validate(HttpServletRequest request) throws AuthenticationException {
        String requestCaptcha = request.getParameter("verifyCode");
        String genCaptcha = (String) request.getSession().getAttribute("verifyCode");
        if (StringUtils.isEmpty(requestCaptcha)) {
            throw new AuthenticationServiceException("请输入验证码！");
        }
        if (genCaptcha == null) {
            throw new AuthenticationServiceException("验证码不存在");
        }
        if (!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {
            throw new AuthenticationServiceException("验证码错误！");
        }
        request.getSession().removeAttribute("verifyCode");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", e);
        response.sendRedirect("/admin/login-error");
    }

}
