package com.example.member.controller;

import com.example.member.repository.MemberRepository;
import com.example.member.service.BoardService;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Transactional
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = "/boardList")
    public String toBoard(Model model) {
        return "board/boardList";
    }



}
