package com.water.site.controller;

import com.water.site.dto.EmailDto;
import com.water.site.entity.UserEntity;
import com.water.site.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpRequest;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "join";
    }
    @PostMapping("/join")
    public String joinSubmit (@ModelAttribute UserEntity user, HttpServletRequest req, Model model) {
        try {
            userService.register(user);
            model.addAttribute("message","가입 성공. 이메일을 확인하여 인증을 완료하세요.");
            return "emailSend";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "join";
        }
    }
}
