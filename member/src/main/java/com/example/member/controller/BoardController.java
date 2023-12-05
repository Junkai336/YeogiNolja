package com.example.member.controller;

import com.example.member.dto.BoardDto;
import com.example.member.dto.CommentDto;
import com.example.member.entity.Board;
import com.example.member.repository.BoardRepository;
import com.example.member.repository.MemberRepository;
import com.example.member.service.BoardService;
import com.example.member.service.CommentService;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CommentService commentService;

    @GetMapping(value = "/boardList")
    public String toBoard(Model model) {
        List<BoardDto> boardDtoList = boardService.boardDtoList();
        model.addAttribute("boardDtoList", boardDtoList);

        return "board/boardList";
    }

    @GetMapping(value = "/boardWrite")
    public String newBoard(Model model) {

        BoardDto boardDto = new BoardDto();
        model.addAttribute("boardDto", boardDto);
        return "board/boardWrite";

    }

    @PostMapping(value = "/boardCreate")
    public String createBoard(@Valid BoardDto boardDto, BindingResult result, Principal principal, Model model) {
        String email= principal.getName();
        try {
            boardService.saveBoard(boardDto, email);
        }catch (Exception e){
            model.addAttribute(e.getStackTrace());
        }



        //redirect : 브라우저가 해당 URL로 재요청
        return "redirect:/board/boardList";
    }

    @GetMapping(value = "/{id}")
    public String show (@PathVariable Long id, Model model) {
        BoardDto boardDto =  boardService.findBoard(id);
        // model에 선택한 글의 id를 가지고 글의 내용을 들고 있는 Dto 객체를 추가
        model.addAttribute("boardDto", boardDto);
        // 글의 id값을 가지고 있는 comment를 List로 하여 model에 추가
        List<CommentDto> commentDtoList = commentService.commentDtoList(id);
        model.addAttribute("commentList", commentDtoList);
        // 새로 입력받을 commentDto 객체를 넘겨준다.
        model.addAttribute("newCommentDto", new CommentDto());
        return "board/boardContents";
    }

//    @GetMapping(value = "/{id}/boardEdit")
//    public String edit(@PathVariable Long id, Model model) {
//        Board boardEntity = boardRepository.findById(id).orElse(null);
//        model.addAttribute("board",boardEntity);
//        return "board/boardEdit";
//    }

    @GetMapping(value = "/{id}/boardEdit")
    public String edit(@PathVariable Long id, Model model) {
        BoardDto boardDto = boardService.findBoard(id);
        model.addAttribute("boardDto", boardDto);

        return "board/boardEdit";
    }

    @PostMapping(value = "/boardUpdate")
    public String update(@Valid BoardDto boardDto, BindingResult result, Model model) {

        try{
            boardService.boardUpdate(boardDto);
        }catch (Exception e){
            model.addAttribute("errorMsg", result.getFieldError());
        }


        return "redirect:/board/boardList";
    }

    @GetMapping(value = "/{id}/boardDelete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Board target = boardRepository.findById(id).orElse(null);

        if(target != null) {
            boardRepository.delete(target);
            rttr.addFlashAttribute("boardSuccessMsg", "삭제가 완료되었습니다.");
        }


        return "redirect:/board/boardList";
    }


}