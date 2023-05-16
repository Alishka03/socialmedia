package com.example.social.pojo;

import com.example.social.entities.Post;
import lombok.Builder;
import lombok.Data;

@Data
public class PostResponse {
    private boolean likedByAuthUser;
    private Post post;
}
