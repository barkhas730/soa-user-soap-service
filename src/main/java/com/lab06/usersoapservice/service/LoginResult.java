package com.lab06.usersoapservice.service;

public class LoginResult {

    private final boolean success;
    private final String token;
    private final Long userId;
    private final String message;

    public LoginResult(boolean success, String token, Long userId, String message) {
        this.success = success;
        this.token = token;
        this.userId = userId;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
