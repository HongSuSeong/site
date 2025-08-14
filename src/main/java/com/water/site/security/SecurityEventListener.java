package com.water.site.security;

import com.water.site.entity.LoginHistory;
import com.water.site.repository.LoginHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SecurityEventListener {

    private final LoginHistoryRepository loginHistoryRepository;
    private final HttpServletRequest request;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        var username = event.getAuthentication().getName();
        loginHistoryRepository.save(LoginHistory.builder()
                .username(username)
                .ipAddress(request.getRemoteAddr())
                .loginAt(LocalDateTime.now())
                .success(true)
                .eventType("LOGIN")
                .build());
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String username = (event.getAuthentication() != null) ? event.getAuthentication().getName() : null;
        loginHistoryRepository.save(LoginHistory.builder()
                .username(username)
                .ipAddress(request.getRemoteAddr())
                .loginAt(LocalDateTime.now())
                .success(false)
                .eventType("LOGIN_FAIL")
                .build());
    }

    @EventListener
    public void onLogout(LogoutSuccessEvent event) {
        String username = (event.getAuthentication() != null) ? event.getAuthentication().getName() : null;
        loginHistoryRepository.save(LoginHistory.builder()
                .username(username)
                .ipAddress(request.getRemoteAddr())
                .loginAt(LocalDateTime.now())
                .success(true)
                .eventType("LOGOUT")
                .build());
    }
}

