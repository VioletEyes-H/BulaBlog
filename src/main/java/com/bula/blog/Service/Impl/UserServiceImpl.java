package com.bula.blog.Service.Impl;

import com.bula.blog.Entity.Roles;
import com.bula.blog.Entity.Users;
import com.bula.blog.Repository.UserRepository;
import com.bula.blog.Service.UserService;
import com.bula.blog.Util.Result;
import com.bula.blog.Util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users users = this.findUser(s);
        if (users == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<Roles> roles = users.getRolesSet();
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAuth()));
        }
        return new User(users.getUsername(), users.getPassword(), authorities);
    }


    /**
     * condition 如果返回了null就不缓存
     *
     * @param users
     * @return
     */
    @Override
    @Transactional
    @CachePut(value = "redisCache", key = "#result.username", condition = "#result != 'null'")
    public Users saveUsers(Users users) {
        Users u = this.findUser(users.getUsername());
        if (u == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encode = passwordEncoder.encode(users.getPassword().trim());
            users.setPassword(encode);
            return userRepository.save(users);
        }
        return null;
    }

    /**
     * 查找用户，先通过缓存查找
     *
     * @param username
     * @return
     */
    @Override
    @Cacheable(value = "redisCache", key = "#username", condition = "#result!=null")
    public Users findUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Result updatePassword(String oldPwd, String newPwd, String username) {
        Users users = userRepository.findByUsername(username);
        if (oldPwd.equals(users.getPassword())) {
            users.setPassword(newPwd);
            return ResultGenerator.genSuccessResult("更新密码成功");
        } else {
            return ResultGenerator.genFailResult("原密码错误");
        }
    }
}
