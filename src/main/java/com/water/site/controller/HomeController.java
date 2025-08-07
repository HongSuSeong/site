package com.water.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home () {
        return "home";
    }
}
