package com.bula.blog.Config;

import com.bula.blog.Filter.CaptchaFilter;
import com.bula.blog.Filter.SuccessHandler;
import com.bula.blog.Service.UserService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] PERMIT_LIST = {"/css/**", "/js/**", "/fonts/**", "/admin/dist/**", "/admin/plugins/**", "/admin/login", "/admin/login-error"};

    private final String[] ROLE_ADMIN_LIST = {"/admin/**"};
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private SuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(PERMIT_LIST).permitAll()
                .antMatchers(ROLE_ADMIN_LIST).hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/admin/login")
                .failureUrl("/admin/login-error")
                .successHandler(successHandler)
                .and()
                .logout()
                .logoutUrl("/admin/logout")
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .sameOrigin();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * DaoAuthenticationProvider是SpringSecurity提供的AuthenticationProvider实现类
     *
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        //创建DaoAuthenticationProvider对象
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //不需要隐藏"用户未找到"异常
        provider.setHideUserNotFoundExceptions(false);
        //通过重写configure方法添加自定义的认证方式
        provider.setUserDetailsService(userService);
        //设置密码加密程序认证
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("abc1232"));
    }

}
