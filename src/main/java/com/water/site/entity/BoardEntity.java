package com.water.site.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOARD")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GEN")
    @SequenceGenerator(name = "BOARD_SEQ_GEN", sequenceName = "BOARD_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    /**
     * 글 비밀번호(옵션): 수정/삭제 시 추가 검증용.
     * 보안을 위해 반드시 해시 저장(PasswordEncoder).
     */
    @Column(name = "post_password", length = 200)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0;
        this.likeCount = 0;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}


