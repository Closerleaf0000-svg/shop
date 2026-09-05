package com.example.shop.controller;

import com.example.shop.dto.LoginRequest;
import com.example.shop.entity.Member;
import com.example.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LoginController {

    private final MemberService memberService;

    // 登入
    @PostMapping("/login")
    public Map<String, Object> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // 呼叫 Service 驗證帳號密碼
        Member loginMember = memberService.login(
                request.getUsername(),
                request.getPassword());

        // 登入失敗
        if (loginMember == null) {

            result.put("success", false);
            result.put(
                    "message",
                    "帳號或密碼錯誤");

            return result;
        }

        // 登入成功將會員資訊存入 Session
        session.setAttribute(
                "memberId",
                loginMember.getMemberId());

        session.setAttribute(
                "username",
                loginMember.getUsername());

        session.setAttribute(
                "email",
                loginMember.getEmail());

        session.setAttribute(
                "role",
                loginMember.getRole());

        // 回傳登入結果給 Vue
        result.put("success", true);
        result.put("message", "登入成功");
        result.put(
                "memberId",
                loginMember.getMemberId());
        result.put(
                "username",
                loginMember.getUsername());
        result.put(
                "email",
                loginMember.getEmail());
        result.put(
                "role",
                loginMember.getRole());

        return result;
    }

    // 登出
    @PostMapping("/logout")
    public Map<String, Object> logout(
            HttpSession session) {

        // 清除 Session
        session.invalidate();

        Map<String, Object> result = new HashMap<>();

        result.put("success", true);
        result.put(
                "message",
                "登出成功");

        return result;
    }
}