package com.example.social.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @Size(max = 255)
    private String content;
}
