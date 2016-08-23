package com.roy.controller;

import com.roy.model.UserEntity;
import com.roy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
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
public class MainController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String getUsers(ModelMap modelMap) {
        //查询user表中所有记录
        List<UserEntity> userEntityList = userRepository.findAll();
        //将所有记录传递给要返回的jsp页面,放在userList中
        modelMap.addAttribute("userList",userEntityList);
        return "admin/users";
    }
    @RequestMapping(value="admin/users/add", method = RequestMethod.GET)
    public String addUser() {
        //跳转到 admin/addUser.jsp页面
        return "admin/addUser";
    }

    /**
     * 向数据库中添加用户信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value="/admin/users/addP" , method = RequestMethod.POST)
    public String addUserPost(@ModelAttribute("user") UserEntity userEntity) {

        //数据库中添加一个用户，该步暂时不会刷新缓存
//        userRepository.save(userEntity);

        //数据库中添加一个用户，并立即刷新缓存
        userRepository.saveAndFlush(userEntity);

        return "redirect:/admin/users";
    }

    /**
     * 查看用户详情
     *@PathVariable可以收集url中的变量，需匹配的变量用{}括起来
     * 例如：访问 localhost:8080/admin/users/show/1 ，将匹配 id = 1
     */
    @RequestMapping(value="/admin/users/show/{id}" , method = RequestMethod.GET)
    public String showUser(@PathVariable("id") Integer userId, ModelMap modelMap) {
        //找到userId所表示的用户
        UserEntity userEntity = userRepository.findOne(userId);

        //传递给请求页面
        modelMap.addAttribute("user", userEntity);
        return "admin/userDetail";
    }

    //更新用户信息 操作
    @RequestMapping(value="/admin/users/update/{id}",method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") Integer userId, ModelMap modelMap) {
        //找到userId所表示的用户
        UserEntity userEntity = userRepository.findOne(userId);

        //传递给请求页面
        modelMap.addAttribute("user" , userEntity);

        return "admin/updateUser";
    }

    //更新用户信息
    @RequestMapping(value="/admin/users/updateP" , method = RequestMethod.POST)
    public String updateUserPost(@ModelAttribute("userP") UserEntity user) {
        //更新用户信息
        userRepository.updateUser(user.getId(),user.getNickname(),user.getFirstName(),user.getLastName(),user.getPassword());
        userRepository.flush();
        return "redirect:/admin/users";
    }

    //删除用户
    @RequestMapping(value = "/admin/users/delete/{id}" , method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Integer userId) {
        //删除id为userId的用户
        userRepository.delete(userId);
        //立即刷新
        userRepository.flush();

        return  "redirect:/admin/users";
    }
}
