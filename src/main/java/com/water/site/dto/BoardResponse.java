package com.water.site.dto;

import java.time.LocalDateTime;

public record BoardResponse(
        Long id,
        String title,
        String content,
        String authorUsername,
        int viewCount,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}