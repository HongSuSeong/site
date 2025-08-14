package com.water.site.security;

import com.water.site.entity.LoginHistory;
import com.water.site.repository.LoginHistoryRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class LoginEventListener {

    private final LoginHistoryRepository loginHistoryRepository;

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        LoginHistory log = new LoginHistory();
        log.setUsername(event.getAuthentication().getName());
        log.setSuccess(true);
        log.setIpAddress(getClientIP()); // HttpServletRequest에서 추출
        loginHistoryRepository.save(log);
    }

    @EventListener
    public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        LoginHistory log = new LoginHistory();
        log.setUsername(event.getAuthentication().getName());
        log.setSuccess(false);
        log.setIpAddress(getClientIP());
        loginHistoryRepository.save(log);
    }

    private String getClientIP() {
        // 현재 request에서 IP 추출
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            return attrs.getRequest().getRemoteAddr();
        }
        return "UNKNOWN";
    }
}
