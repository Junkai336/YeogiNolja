package com.example.member.controller;

import com.example.member.dto.BoardDto;
import com.example.member.entity.Board;
import com.example.member.repository.BoardRepository;
import com.example.member.repository.MemberRepository;
import com.example.member.service.BoardService;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequiredArgsConstructor
@Transactional
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping(value = "/boardList")
    public String toBoard(Model model) {
        return "board/boardList";
    }

    @GetMapping(value = "/boardWrite")
    public String newBoard(Model model) {
        BoardDto boardDto =new BoardDto();
        model.addAttribute("boardDto", boardDto);
        return "board/boardWrite";
    }

    @PostMapping(value = "/boardCreate")
    public String createBoard(BoardDto boardDto) {


    boardService.saveBoard(boardDto);


//        Board saved = boardService.create(boardDto);

        //redirect : 브라우저가 해당 URL로 재요청
//        return "redirect:/board/" + savedBoard.getId();
//return "board/boardList";
        return "/";
    }
}
