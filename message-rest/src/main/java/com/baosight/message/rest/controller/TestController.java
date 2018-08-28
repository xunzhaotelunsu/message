package com.baosight.message.rest.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    @GetMapping("/test")
    public String getStr(HttpServletRequest request){


        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
