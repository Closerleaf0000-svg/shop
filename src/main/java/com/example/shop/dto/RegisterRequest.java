package com.example.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterRequest {

    // 帳號不可為空白
    @NotBlank(message = "帳號名稱不可空白")
    private String username;

    // 密碼：
    // 6～20 碼
    // 至少包含一個英文字母
    // 至少包含一個數字
    @NotBlank(message = "密碼不可空白")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", message = "密碼需為 6~20 碼，且至少包含一個英文字母與一個數字")
    private String password;

    // 確認密碼
    @NotBlank(message = "確認密碼不可空白")
    private String confirmPassword;

    // Email 格式驗證
    @NotBlank(message = "Email 不可空白")
    @Email(message = "Email 格式錯誤")
    private String email;

    // 台灣手機格式：09 開頭，共 10 碼
    @NotBlank(message = "手機號碼不可空白")
    @Pattern(regexp = "^09\\d{8}$", message = "手機號碼格式錯誤，請輸入09開頭的10位數手機號碼")
    private String phone;

    public RegisterRequest() {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}