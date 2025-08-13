package com.water.site.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardPasswordRequest {
    @NotBlank
    private String password; // 삭제/수정 시 필요할 때 사용
}