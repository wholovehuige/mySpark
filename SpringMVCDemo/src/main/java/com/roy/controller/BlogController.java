package com.roy.controller;

import com.roy.model.BlogEntity;
import com.roy.model.UserEntity;
import com.roy.repository.BlogRepository;
import com.roy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
@Controller
public class BlogController {
    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    // 查看所有博文
    @RequestMapping(value = "/admin/blog", method = RequestMethod.GET)
    public String showBlogs(ModelMap modelMap) {
        List<BlogEntity> blogList = blogRepository.findAll();
        modelMap.addAttribute("blogList", blogList);
        return "admin/blog";
    }


    // 添加博文
    @RequestMapping(value = "/admin/blog/add", method = RequestMethod.GET)
    public String addBlog(ModelMap modelMap) {
        List<UserEntity> userList = userRepository.findAll();
        // 向jsp注入用户列表
        modelMap.addAttribute("userList", userList);
        return "admin/addBlog";
    }

    // 添加博文，POST请求，重定向为查看博客页面
    @RequestMapping(value = "/admin/blog/addP", method = RequestMethod.POST)
    public String addBlogPost(@ModelAttribute("blog") BlogEntity blogEntity) {
        // 打印博客标题
        System.out.println(blogEntity.getTitle());
        // 打印博客作者
        System.out.println(blogEntity.getUserByUserId().getNickname());
        // 存库
        blogRepository.saveAndFlush(blogEntity);
        // 重定向地址
        return "redirect:/admin/blog";
    }

    // 查看博文详情，默认使用GET方法时，method可以缺省
    @RequestMapping("/admin/blog/show/{id}")
    public String showBlog(@PathVariable("id") int id, ModelMap modelMap) {
        BlogEntity blog = blogRepository.findOne(id);
        modelMap.addAttribute("blog", blog);
        return "admin/blogDetail";
    }


}
