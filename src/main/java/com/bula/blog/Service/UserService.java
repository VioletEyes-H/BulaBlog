package com.bula.blog.Service;


import com.bula.blog.Entity.Users;
import com.bula.blog.Util.Result;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Users saveUsers(Users user);

    Users findUser(String username);

    Result updatePassword(String oldPwd, String newPwd,String username);
}
