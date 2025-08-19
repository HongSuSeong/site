package com.water.site.dto;

import java.time.LocalDateTime;

public record CommentResponse (
    Long id,
    String content,
    LocalDateTime createdAt
) {
}
