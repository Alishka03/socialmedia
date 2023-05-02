package com.example.social.pojo;

import com.example.social.entities.User;

public class ProfileResponse {
    private String message;
    private User user;

    public ProfileResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
