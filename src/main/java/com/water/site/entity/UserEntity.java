package com.water.site.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인 ID

    private String password;
    private String email;
    private String name;
    private String role; // ROLE_USER, ROLE_ADMIN

    private String provider;     // local, naver
    private String providerId;   // 네이버에서 받은 ID
}
