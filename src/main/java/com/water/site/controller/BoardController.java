package com.water.site.controller;

import com.water.site.dto.BoardResponse;
import com.water.site.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @PageableDefault(size = 10, sort = "id") Pageable pageable,
                       Model model) {
        Page<BoardResponse> page = boardService.list(q, pageable);
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "boards/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        BoardResponse dto = boardService.get(id, true);
        model.addAttribute("board", dto);
        return "boards/detail";
    }
}
