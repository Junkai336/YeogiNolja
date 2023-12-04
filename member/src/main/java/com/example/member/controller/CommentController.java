package com.example.member.controller;

import com.example.member.dto.ArticleDto;
import com.example.member.dto.CommentDto;
import com.example.member.service.ArticleService;
import com.example.member.service.CommentService;
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
    private final ArticleService articleService;


    @GetMapping("/board/{article_id}/comment")
    public String newComment(@PathVariable Long article_id,
                             @Valid CommentDto commentDto, BindingResult result,
                             Principal principal
<<<<<<< HEAD
                             ,Model model){
        System.out.println(article_id);
=======
            ,Model model){
        System.out.println(board_id);
>>>>>>> master
        String email = principal.getName().toString();
        commentService.newComment(commentDto, email, article_id);
        try {
            ArticleDto articleDto = articleService.findArticle(article_id);
            model.addAttribute("boardDto", articleDto);
            // CommentList 가 추가된 board 를 모델에 담아 리턴
        }catch (Exception e){
            model.addAttribute("errorMessage", result.getFieldError());

        }

        return "redirect:/board/"+article_id;

    }

    @GetMapping(value = "/board/{article_id}/commentDelete/{comment_id}")
    public String commentDelete(@PathVariable("article_id") Long article_id, @PathVariable("comment_id") Long commentId, Principal principal, Model model){
        String email =principal.getName();
        // comment 의 id 와 접근자의 email을 받아 유효성 검사를 한다.
        try {
            commentService.validation(commentId, email);
            commentService.deleteComment(commentId);
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/board/"+article_id;
    }





}