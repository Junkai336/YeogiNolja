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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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
        List<BoardDto> boardDtoList = boardService.boardDtoList();
        model.addAttribute("boardList", boardDtoList);

        return "board/boardList";
    }

    @GetMapping(value = "/boardWrite")
    public String newBoard(Model model, Principal principal) {

//        if(principal != null) {
//        } else {
//            List<BoardDto> boardDtoList = boardService.boardDtoList();
//            model.addAttribute("boardList", boardDtoList);
//           model.addAttribute("boardErrorMsg", "로그인된 사용자만 게시글을 작성할 수 있습니다.");
//           return "board/boardList";
//        }

        BoardDto boardDto = new BoardDto();
        model.addAttribute("boardDto", boardDto);
        return "board/boardWrite";

    }

    @PostMapping(value = "/boardCreate")
    public String createBoard(@Valid BoardDto boardDto, BindingResult result, Principal principal, Model model, RedirectAttributes rttr) {
        String email= principal.getName();
    try {
        boardService.saveBoard(boardDto, email);
        rttr.addFlashAttribute("boardSuccessMsg", "게시글 생성이 완료되었습니다.");
    }catch (Exception e){
      model.addAttribute(result.getFieldError());
    }


        //redirect : 브라우저가 해당 URL로 재요청
        return "redirect:/board/boardList";
    }

    @GetMapping(value = "/{id}")
    public String show (@PathVariable Long id, Model model) {
        Board boardEntity = boardRepository.findById(id).orElse(null);
//        Board boardEntity = boardService.findId(id);

        model.addAttribute("boards", boardEntity);

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
        Board boardEntity = boardRepository.findById(id).orElse(null);
//        Board boardEntity = boardService.findId(id);

        model.addAttribute("board",boardEntity);
        return "board/boardEdit";
    }

    @PostMapping(value = "/boardUpdate")
    public String update(BoardDto boardDto, RedirectAttributes rttr) {
        Board boardEntity = Board.builder()
                .id(boardDto.getId())
                .boardTitle(boardDto.getBoardTitle())
                .content(boardDto.getContent())
                .boardCategoryStatus(boardDto.getBoardCategoryStatus())
                .member(boardDto.getMember())
                .build();

        Board target = boardRepository.findById(boardEntity.getId()).orElse(null);

        if(target != null) {
            boardRepository.save(boardEntity);
            rttr.addFlashAttribute("boardSuccessMsg", "게시글 생성이 완료되었습니다.");
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

//        Board target = boardService.findId(id);
//
//        Board result = boardService.deleteBoard(target);
//
//        if(result == null) {
//            rttr.addFlashAttribute("boardSuccessMsg", "삭제가 완료되었습니다.");
//        }

        return "redirect:/board/boardList";
    }


}
