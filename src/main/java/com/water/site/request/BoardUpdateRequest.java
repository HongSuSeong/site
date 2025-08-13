package com.water.site.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardUpdateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String passwordForEdit;
}
