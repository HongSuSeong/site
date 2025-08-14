package com.water.site.controller;

import com.water.site.dto.BoardResponse;
import com.water.site.request.BoardCreateRequest;
import com.water.site.request.BoardUpdateRequest;
import com.water.site.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /** 게시글 목록 */
    @GetMapping("/list")
    public String list(@RequestParam(required = false, value = "q") String q,
                       @PageableDefault(size = 10, sort = "id") Pageable pageable,
                       Model model) {
        model.addAttribute("page", boardService.list(q, pageable));
        model.addAttribute("q", q);
        return "boards/list";
    }

    /** 게시글 작성 화면 */
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("board", new BoardCreateRequest());
        return "boards/form";
    }

    /** 게시글 등록 처리 */
    @PostMapping("/create")
    public String create(@ModelAttribute BoardCreateRequest req,
                         @AuthenticationPrincipal UserDetails user) {
        boardService.create(req, user.getUsername());
        return "redirect:/boards/list";
    }

    /** 게시글 상세 보기 */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        BoardResponse board = boardService.get(id, true);
        model.addAttribute("board", board);
        return "boards/view";
    }

    /** 게시글 수정 화면 */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardResponse board = boardService.get(id, false); // 조회수 증가 X

        BoardUpdateRequest req = new BoardUpdateRequest();
        req.setId(board.id());
        req.setTitle(board.title());      // record의 getter는 title()임
        req.setContent(board.content());


        model.addAttribute("board", req);
        //model.addAttribute("id", id);

        return "boards/form";
    }

    /** 게시글 수정 처리 */
    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("board") BoardUpdateRequest req,
                         @AuthenticationPrincipal UserDetails user,
                         Model model) {
        try {
            boardService.update(id, req, user.getUsername());
            return "redirect:/boards/view/" + id;
        } catch (AccessDeniedException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());

            // 원래 데이터 유지
            BoardResponse board = boardService.get(id, false);
            req.setTitle(board.title());
            req.setContent(board.content());

            return "boards/form";
        }
    }

    /** 게시글 삭제 처리 */
    @PostMapping("/delete-ajax/{id}")
    public ResponseEntity<Map<String, Object>> deleteAjax(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false, name = "passwordForDelete") String passwordForDelete
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            boardService.delete(id, user.getUsername(), passwordForDelete);
            result.put("success", true);
        } catch (AccessDeniedException | IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
