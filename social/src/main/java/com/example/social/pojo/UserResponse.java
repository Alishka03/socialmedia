package com.example.social.pojo;

import com.example.social.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private User user;
    private boolean followedByAuthUser;
}
