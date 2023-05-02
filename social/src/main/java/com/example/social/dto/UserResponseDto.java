package com.example.social.dto;

import com.example.social.entities.Post;
import com.example.social.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private int id;
    private String name;
    private String surname;

    private List<Post> postsLists;
    private List<User> followingUsers;
    private List<User> followerUsers;
}
