package com.example.member.article.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

// 댓글 생성
    @PostMapping("{article_id}/new")
    public String newComment(@PathVariable("article_id")Long article_id,
                             @Valid CommentDto commentDto, BindingResult result,
                             Model model, Principal principal){
        String email = principal.getName();
        if(result.hasErrors()){
            return "article/detail";
        }
        try{
            commentService.createComment(article_id, commentDto, email);

        }catch (Exception e){
            model.addAttribute(e.getMessage());
        }
            return "redirect:/article/detail";
    }


}
