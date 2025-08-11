package com.water.site.controller;

import com.water.site.entity.Board;
import com.water.site.repository.BoardRepository;
import com.water.site.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    public BoardController(BoardRepository boardRepository, BoardService boardService) {
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public String list (Model model) {
        List<Board> boards = boardRepository.findAllByOrderByCreatedDateDesc();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/write")
    public String writeForm (Model model) {
        model.addAttribute("board",new Board());
        return "board/write";
    }

    @PostMapping("/write")
    public String write (@ModelAttribute @Valid Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("오류");
            return "board/write";  // 유효하지 않으면 작성 페이지로 다시 돌아감
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }

    @GetMapping("/detail/{id}")
    public String detail (@PathVariable("id") Long id,Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "/board/detail";
    }

    @GetMapping("/modify/{id}")
    public String modifyForm (@PathVariable("id") Long id,Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "board/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify (@PathVariable("id") Long id, @ModelAttribute @Valid Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("오류");
            return "board/modify";  // 유효하지 않으면 작성 페이지로 다시 돌아감
        }
        System.out.println("성공");
        boardService.update(id, board);
        return "redirect:/board/detail/" + id;
    }
}