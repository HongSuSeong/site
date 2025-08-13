package com.water.site.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String name;
}
