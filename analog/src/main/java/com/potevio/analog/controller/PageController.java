package com.potevio.analog.controller;

import com.potevio.analog.pojo.TerminalData;
import com.potevio.analog.service.AnalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/page")
public class PageController {

    @Autowired
    private AnalogService service;

    @RequestMapping("/main")
    public String main(Model model) {
        List<TerminalData> terminalList = service.getTerminalList();
        model.addAttribute("resultList", terminalList);
        return "mainpage";
    }

}
