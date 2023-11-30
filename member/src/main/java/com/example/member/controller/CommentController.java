package com.example.member.controller;

import com.example.member.dto.BoardDto;
import com.example.member.dto.CommentDto;
import com.example.member.service.BoardService;
import com.example.member.service.CommentService;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;


    @GetMapping("/board/{board_id}/comment")
    public String newComment(@PathVariable Long board_id,
                             @Valid CommentDto commentDto, BindingResult result,
                             Principal principal
                             ,Model model){
        System.out.println(board_id);
        String email = principal.getName();
        try {
            BoardDto boardDto = boardService.findBoard(board_id);
            // CommentList 가 추가된 board 를 모델에 담아 리턴
        }catch (Exception e){
            model.addAttribute("errorMessage", result.getFieldError());

        }



        return "redirect:/board/"+board_id;

    }



}
