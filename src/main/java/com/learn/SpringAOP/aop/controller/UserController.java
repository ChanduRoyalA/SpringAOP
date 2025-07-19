package com.learn.SpringAOP.aop.controller;


import com.learn.SpringAOP.aop.model.User;
import com.learn.SpringAOP.aop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Collection<User> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/add/user")
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }
}
