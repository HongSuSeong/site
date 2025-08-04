package com.water.site.controller;

import com.water.site.repository.BoardRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final BoardRepository boardRepository;

    public HomeController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @RequestMapping("/home")
    public String home () {
        return "home.html";
    }

    @RequestMapping("/board")
    public String board() {
        System.out.println(boardRepository.findAll());
        return "board.html";
    }
}
