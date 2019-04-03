package com.fs.thymeleafdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/index1")
    public String index1(Model model){
        model.addAttribute("result", "后台返回index1");
        return "result";
    }



}
