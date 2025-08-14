package com.water.site.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "login_history")
public class LoginHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ipAddress;
    private LocalDateTime loginAt;
    private boolean success; // true: 로그인 성공, false: 실패
    private String eventType; // LOGIN, LOGOUT
}

