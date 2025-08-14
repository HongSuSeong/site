package com.water.site.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
public class BoardCreateRequest {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private String password;
}
