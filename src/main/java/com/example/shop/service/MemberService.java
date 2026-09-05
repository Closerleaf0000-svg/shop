package com.example.shop.service;

import com.example.shop.entity.Member;
import com.example.shop.mapper.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    // MyBatis Mapper
    private final MemberMapper memberMapper;

    // Constructor Injection
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    // 註冊會員
    public Member register(Member member) {

        // Email 是否已存在
        if (memberMapper.existsByEmail(member.getEmail())) {

            throw new RuntimeException("Email 已被註冊");
        }

        // 新註冊會員一律為 USER
        member.setRole("USER");

        logger.info("會員註冊: username={}",
                member.getUsername());

        // MyBatis 新增資料
        int result = memberMapper.insert(member);

        if (result == 0) {

            throw new RuntimeException("會員註冊失敗");
        }

        logger.info("註冊成功: id={}",
                member.getMemberId());

        return member;
    }

    // 登入會員
    public Member login(String username, String password) {

        Optional<Member> optionalMember = memberMapper.findByUsername(username);

        // 帳號不存在
        if (optionalMember.isEmpty()) {

            logger.warn("登入失敗，帳號不存在: {}",
                    username);

            return null;
        }

        Member member = optionalMember.get();

        // 密碼錯誤
        if (!member.getPassword().equals(password)) {

            logger.warn("登入失敗，密碼錯誤: {}",
                    username);

            return null;
        }

        logger.info("登入成功: {}",
                username);

        return member;
    }

    // 檢查帳號是否存在
    public boolean isUsernameTaken(String username) {

        return memberMapper.findByUsername(username)
                .isPresent();
    }

    // 檢查 Email 是否存在
    public boolean isEmailTaken(String email) {

        return memberMapper.existsByEmail(email);
    }
}