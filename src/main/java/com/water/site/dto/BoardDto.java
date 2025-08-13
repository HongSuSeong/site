package com.water.site.dto;

import java.time.LocalDateTime;

public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private int viewCount;
    private LocalDateTime createdAt;
}
