package com.learn.SpringAOP.aop.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {


    @GetMapping("/")
    public String  getHello(){
        return "Hello AOP";
    }
}
