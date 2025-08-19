package com.water.site.controller;

import com.water.site.entity.UserEntity;
import com.water.site.security.CustomUserDetails;
import com.water.site.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/add")
    public String addComment(@RequestParam("boardId") Long boardId,
                             @RequestParam("content") String content,
                             @AuthenticationPrincipal CustomUserDetails user) {

        if (user == null) return "redirect:/login";

        UserEntity userEntity = user.getUser(); // CustomUserDetails에 getEntity() 추가
        commentService.addComment(boardId, content, userEntity);

        return "redirect:/boards/view/" + boardId;
    }
}

