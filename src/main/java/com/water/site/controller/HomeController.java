package com.water.site.controller;

import com.water.site.entity.Board;
import com.water.site.repository.BoardRepository;
import com.water.site.response.BoardResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @RequestMapping("/home")
    public String home () {
        return "home.html";
    }
}
