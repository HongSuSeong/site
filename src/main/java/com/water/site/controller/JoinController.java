package com.water.site.controller;

import com.water.site.entity.UserEntity;
import com.water.site.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String joinSubmit (@ModelAttribute UserEntity user) {
        userService.register(user);
        return "redirect:login";
    }
}
