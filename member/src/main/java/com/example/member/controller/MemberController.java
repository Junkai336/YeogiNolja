package com.example.member.controller;

import com.example.member.repository.MemberRepository;
import com.example.member.service.MemberService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Transactional
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/join")
    public String toJoin() {
        return "/member/join";
    }

    @GetMapping(value = "/login")
    public String toLogin() {
        return "/member/login";
    }
}
