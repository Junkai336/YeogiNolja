package com.example.member.article.comment;

import com.example.member.article.ArticleDto;
import com.example.member.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;


@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

// 댓글 생성

    @PostMapping("/article/{article_id}/comment")
    public void newComment(@PathVariable("article_id") Long article_id,
                             @RequestBody CommentDto commentDto, BindingResult result,
                             Principal principal, Model model){
        System.out.println(article_id);
        System.out.println(commentDto.getComment());
        String email = principal.getName().toString();
        commentService.newComment(commentDto, email, article_id);
        try {
            ArticleDto articleDto = articleService.findArticle(article_id);
            // CommentList 가 추가된 board 를 모델에 담아 리턴
        }catch (Exception e){
            model.addAttribute("errorMessage", result.getFieldError());

        }


    }

    @GetMapping(value = "/article/{article_id}/commentDelete/{comment_id}")
    public String commentDelete(@PathVariable("article_id") Long article_id, @PathVariable("comment_id") Long commentId, Principal principal, Model model){
        String email =principal.getName();
        // comment 의 id 와 접근자의 email을 받아 유효성 검사를 한다.
        try {
            commentService.validation(commentId, email);
            commentService.deleteComment(commentId);
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/article/"+article_id;
    }

//    @PostMapping(value = "/article/{article_id}/editComment/{comment_id}")
//    public String commentEdit(@PathVariable("article_id") Long article_id,
//        @PathVariable("comment_id") Long comment_id,@ModelAttribute CommentDto commentDto){
//        commentService.update(comment_id, commentDto);
//
//        return "redirect:/article/"+article_id;
//
//    }

    @PostMapping(value = "/article/{article_id}/commentEdit/{comment_id}")
    public void editComment(@PathVariable("comment_id") Long comment_id, @PathVariable("article_id") Long article_id
            , @RequestBody EditCommentDto editCommentDto){
        System.out.println("view에서 넘어온 내용! : 아이디"+editCommentDto.getId());
        System.out.println("view에서 넘어온 내용! : 내용"+editCommentDto.getComment());
        commentService.update(comment_id, editCommentDto);
    }





}
