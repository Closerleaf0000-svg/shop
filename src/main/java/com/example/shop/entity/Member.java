package com.example.shop.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Member {

    private Long memberId;

    @NotBlank(message = "帳號名稱不可空白")
    private String username;

    @NotBlank(message = "密碼不可空白")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", message = "密碼需為 6~20 碼,且至少包含一個英文字母與一個數字")
    private String password;

    @NotBlank(message = "Email 不可空白")
    @Email(message = "Email 格式錯誤")
    private String email;

    @NotBlank(message = "手機號碼不可空白")
    @Pattern(regexp = "^09\\d{8}$", message = "手機號碼格式錯誤,請輸入09開頭的10位數手機號碼")
    private String phone;

    private String role;

    public Member() {

    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
