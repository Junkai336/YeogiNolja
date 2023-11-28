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
        List<BoardDto> boardEntityList = boardService.boardDtoList();
        model.addAttribute("boardLists", boardEntityList);

        return "board/boardList";
    }

//    @GetMapping(value = "/boardWrite")
//    public String newBoard(Model model) {
//
//        BoardDto boardDto = new BoardDto();
//        model.addAttribute("boardDto", boardDto);
//        return "board/boardWrite";
//
//    }

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
      model.addAttribute(e.getMessage());
    }


        //redirect : 브라우저가 해당 URL로 재요청
        return "redirect:/board/boardList";
    }

    @GetMapping(value = "/{id}")
    public String show (@PathVariable Long id, Model model) {
        Board boardEntity = boardRepository.findById(id).orElse(null);
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
        model.addAttribute("board",boardEntity);
        return "board/boardEdit";
    }

    @PostMapping(value = "/boardUpdate")
    public String update(BoardDto boardDto) {
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

        }
            return "redirect:/board/boardList";
    }

    @GetMapping(value = "/{id}/boardDelete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Board target = boardRepository.findById(id).orElse(null);

        if(target != null) {
            boardRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");
        }

        return "redirect:/board/boardList";
    }


}
