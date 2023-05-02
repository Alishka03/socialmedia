package com.example.social.response;

public class JwtResponse {
    private String status;
    private String token;
    private String type = "Bearer";
    private int id;
    private String username;



    public JwtResponse(String status) {
        this.status = status;
    }

    public JwtResponse(String status, String accessToken, int id, String username) {
        this.status = status;
        this.token = accessToken;
        this.id = id;
        this.username = username;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
