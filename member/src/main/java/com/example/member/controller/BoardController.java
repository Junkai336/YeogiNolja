package com.example.member.controller;

import com.example.member.repository.MemberRepository;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Transactional
public class BoardController {

    @GetMapping(value = "/boardList")
    public String toLogin() {
        return "/board/boardList";
    }

}
