package com.example.social.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateInfoDTO {
    private String name;
    private String surname;
    private String username;
    private String intro;
    private String hometown;
    private String workplace;
    private String password;
}
