package com.bula.blog;

import com.alibaba.fastjson.JSONArray;
import com.bula.blog.Entity.BlogTag;
import com.bula.blog.Entity.Users;
import com.bula.blog.Service.BlogService;
import com.bula.blog.Service.TagService;
import com.bula.blog.Service.UserService;
import com.bula.blog.Util.BlogUtils;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Vo.CountTagVo;
import com.mysql.cj.xdevapi.JsonArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;


    @Test
    void contextLoads() {
    }

    @Test
    void regedit() {
//        Users users = new Users();
//        users.setUsername("bula");
//        users.setPassword("123456");
//        userService.saveUsers(users);
        Users users = userService.findUser("bula");
        System.out.println(users);
    }

    @Test
    void testHotTagsSide() {
        List<CountTagVo> countTagVos = tagService.getHotTagIndexForSide();
        for (CountTagVo countTagVo : countTagVos) {
            System.out.println(countTagVo.getTagName());
        }
    }

    @Test
    void testTagForTagName() throws IllegalAccessException {
        PageResult pageResult = tagService.getTagIndexForBlogs("fff", 1);
        List<BlogTag> blogTags = (List<BlogTag>) pageResult.getList();
        JSONArray jsonValues = new BlogUtils<BlogTag>().BlogVOToString(blogTags);
        System.out.println(jsonValues);
    }

    @Resource
    private BlogService blogService;

    @Test
    void addReading() {
        blogService.addReading(1);
    }
}
