package com.example.shop.controller;

import com.example.shop.dto.RegisterRequest;
import com.example.shop.entity.Member;
import com.example.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RegisterController {

    private final MemberService memberService;

    // 會員註冊
    @PostMapping("/register")
    public Map<String, Object> register(
            @Valid @RequestBody RegisterRequest request) {

        Map<String, Object> result = new HashMap<>();

        // 取得 DTO 中的資料
        String username = request.getUsername();
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();
        String email = request.getEmail();
        String phone = request.getPhone();

        // 確認密碼是否一致
        if (!password.equals(confirmPassword)) {

            result.put("success", false);
            result.put(
                    "message",
                    "兩次輸入的密碼不一致");

            return result;
        }

        // 檢查帳號是否已存在
        if (memberService.isUsernameTaken(username)) {

            result.put("success", false);
            result.put(
                    "message",
                    "這個帳號已經被使用了");

            return result;
        }

        // 檢查 Email 是否已存在
        if (memberService.isEmailTaken(email)) {

            result.put("success", false);
            result.put(
                    "message",
                    "這個 Email 已經被註冊過了");

            return result;
        }

        // 建立 Member Entity
        Member member = new Member();

        member.setUsername(username);
        member.setPassword(password);
        member.setEmail(email);
        member.setPhone(phone);

        // 寫入資料庫
        memberService.register(member);

        // 註冊成功
        result.put("success", true);
        result.put("message", "註冊成功");

        return result;
    }
}