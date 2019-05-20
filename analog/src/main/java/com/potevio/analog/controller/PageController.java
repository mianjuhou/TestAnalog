package com.potevio.analog.controller;

import com.potevio.analog.pojo.TerminalData;
import com.potevio.analog.service.AnalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Controller
@ApiIgnore
public class PageController {

    @Autowired
    private AnalogService service;

    @RequestMapping("/page/main")
    public String main(Model model) {
        List<TerminalData> terminalList = service.getTerminalList();
        model.addAttribute("resultList", terminalList);
        return "mainpage";
    }

    @GetMapping({"/","/index","/index.html"})
    public String index(){
        System.out.println("ok");
        return "/dianli/forms2.html";//static/dianli/forms2.html
    }

}
