package com.water.site.controller;

import com.water.site.dto.EmailDto;
import com.water.site.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/email")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final UserService userService;

    @PostMapping("/send")
    public String mailSend (EmailDto emailDto) throws MessagingException {
        userService.sendEmail(emailDto.getMail());
        return "인증코드가 발송되었습니다.";
    }

    @PostMapping("/verify")
    public String verify(EmailDto emailDto) {
        Boolean isVerify = userService.verifyEmailCode(emailDto.getMail(), emailDto.getVerifyCode());
        return isVerify ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
    }
}
