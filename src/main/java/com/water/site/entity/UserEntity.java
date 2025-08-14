package com.water.site.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique=true)
    private String email;

    @OneToMany(mappedBy = "author")
    private List<BoardEntity> boards = new ArrayList<>();

    private String name;
    private String role;
    private String provider;    // local, naver ...
    private String providerId;
    private boolean enabled;
    private LocalDateTime createdAt = LocalDateTime.now();
}
