package com.water.site.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name="board_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "post_password", length = 200)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(name = "content_search", length = 1000)
    private String contentSearch;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0;
        this.likeCount = 0;
        setContentSearch();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private void setContentSearch() {
        if (this.content != null) {
            this.contentSearch = content.length() > 1000 ? content.substring(0, 1000) : content;
        }
    }
}