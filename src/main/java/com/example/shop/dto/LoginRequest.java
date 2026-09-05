package com.example.shop.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    // 登入帳號
    @NotBlank(message = "帳號名稱不可空白")
    private String username;

    // 登入密碼
    @NotBlank(message = "密碼不可空白")
    private String password;

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}