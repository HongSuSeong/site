package com.water.site.controller;

import com.water.site.entity.UserEntity;
import com.water.site.dto.UserRegisterRequest;
import com.water.site.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String joinSubmit (@ModelAttribute("user") UserRegisterRequest userRegisterRequest, Model model) {
        try {
            userService.register(userRegisterRequest);
            return "home";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "join";
        }
    }

    @PostMapping("/check")
    @ResponseBody
    public boolean idCheck(@RequestParam("username") String username) {
        return userService.idCheck(username);
    }
}
