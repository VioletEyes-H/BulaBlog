package com.bula.blog.Util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bula.blog.Entity.Blog;
import com.bula.blog.Entity.BlogTag;
import com.bula.blog.Vo.SimpleBlogVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.servlet.FilterConfig;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BlogUtils<T> {
    @Autowired
    private static RedisTemplate redisTemplate;

    public static URI getHost(URI uri) {
        URI effectiveURI = null;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable var4) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    /**
     * 复制Blog转为SimpleBlog
     * 截取文章
     *
     * @param blog
     * @return
     */
    public static SimpleBlogVo CopyAndSubForSimpleBlog(Blog blog) {
        SimpleBlogVo simpleBlogVo = new SimpleBlogVo();
        BeanUtils.copyProperties(blog, simpleBlogVo);
        String content = blog.getContent();
        Matcher matcher = Pattern.compile("([\\u4e00-\\u9fa5a-zA-Z]+)").matcher(content);
        if (matcher.find()) {
            content = matcher.group(0);
        }
        if (content.length() >= 90) {
            simpleBlogVo.setSummary(content.substring(0, 88));
        } else {
            simpleBlogVo.setSummary(content);
        }
        simpleBlogVo.setCategory(blog.getCategory().getCategoryName());
        simpleBlogVo.setTag(blog.getTagList().toString());
        return simpleBlogVo;
    }

    /**
     * 复制Blog转为SimpleBlog
     *
     * @param blog
     * @return
     */
    public static SimpleBlogVo CopyForSimpleBlog(Blog blog) {
        SimpleBlogVo simpleBlogVo = new SimpleBlogVo();
        BeanUtils.copyProperties(blog, simpleBlogVo);
        simpleBlogVo.setCategory(blog.getCategory().getCategoryName());
        simpleBlogVo.setTag(TagListToString(blog.getTagList()));
        return simpleBlogVo;
    }

    /**
     * 把TagBlog的Tag转换成List
     *
     * @param list
     * @return
     */
    private static String TagListToString(List<BlogTag> list) {
        List<String> stringList = new ArrayList<>();
        for (BlogTag str : list) {
            stringList.add(str.getTagName());
        }
        return StringUtils.join(stringList, ',');
    }

    /**
     * 使用反射遍历实体类
     *
     * @param data
     * @return
     * @throws IllegalAccessException
     */
    public JSONArray BlogVOToString(List<T> data) {
        JSONArray jsonArray = new JSONArray();
        for (T t : data) {
            Class cls = t.getClass();
            Field[] fields = cls.getDeclaredFields();
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                try {
                    jsonObject.put(f.getName(), f.get(t));//name是属性名，然后get属性值
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public static void refreshRedis(String key) {
        Set<String> keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }
}
