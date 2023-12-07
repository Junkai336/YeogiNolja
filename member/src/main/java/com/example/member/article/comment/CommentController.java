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

    @GetMapping("/article/{article_id}/comment")
    public String newComment(@PathVariable Long article_id,
                             @Valid CommentDto commentDto, BindingResult result,
                             Principal principal
            ,Model model){
        System.out.println(article_id);
        String email = principal.getName().toString();
        commentService.newComment(commentDto, email, article_id);
        try {
            ArticleDto articleDto = articleService.findArticle(article_id);
            model.addAttribute("articleDto", articleDto);
            // CommentList 가 추가된 board 를 모델에 담아 리턴
        }catch (Exception e){
            model.addAttribute("errorMessage", result.getFieldError());

        }

        return "redirect:/article/"+article_id;

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

    @PostMapping(value = "/article/{article_id}/editComment/{comment_id}")
    public String commentEdit(@PathVariable("article_id") Long article_id,
        @PathVariable("comment_id") Long comment_id,@ModelAttribute CommentDto commentDto){
        commentService.update(comment_id, commentDto);

        return "redirect:/article/"+article_id;

    }



    //댓글 수정
    @ResponseBody
    @PostMapping("/article/{articleId}/commentEdit/{commentId}")
    public void update(@PathVariable("articleId") Long article_id,@PathVariable("commentId") Long comment_id,
                         @RequestBody HashMap<String, Object> map){
        //서비스에 위임
        System.out.println(map);
//        CommentDto commentDto = commentService.update(comment_id, dto);

    }

}
