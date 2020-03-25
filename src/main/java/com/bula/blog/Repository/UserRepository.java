package com.bula.blog.Repository;

import com.bula.blog.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);

}
