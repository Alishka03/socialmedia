package com.example.social.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserInfoDto {
    @NotEmpty
    @Size(max = 64)
    private String name;

    @NotEmpty
    @Size(max = 64)
    private String surname;

    @NotEmpty
    @Size(max = 64)
    private String username;

}
