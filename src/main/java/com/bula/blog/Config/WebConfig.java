package com.bula.blog.Config;

import com.bula.blog.Filter.BlogInterceptor;
import com.bula.blog.Util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 添加拦截器
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Autowired
    private BlogInterceptor blogInterceptor;
//    @Autowired
//    private CaptchaInterceptor captchaInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(blogInterceptor)
                .addPathPatterns("/blog/**");

    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //添加静态资源映射，把static映射为/
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }

}
