package com.water.site.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "board_history")
public class BoardHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;
    private String username;
    private String action; // CREATE, UPDATE, DELETE
    private LocalDateTime actionAt;
}
