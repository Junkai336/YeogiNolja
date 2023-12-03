package com.example.member.controller;

import com.example.member.dto.ArticleDto;
import com.example.member.dto.CommentDto;
import com.example.member.entity.Article;
import com.example.member.repository.ArticleRepository;
import com.example.member.service.ArticleService;
import com.example.member.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;
    private final CommentService commentService;

    @GetMapping(value = "/list")
    public String toArticle(Model model) {
        try {
            List<ArticleDto> articleDtoList = articleService.articleDtoList();
            model.addAttribute("articleDtoList", articleDtoList);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }


        return "article/list";
    }

    @GetMapping(value = "/new")
    public String newArticle(Model model) {

        ArticleDto articleDto = new ArticleDto();
        model.addAttribute("ArticleDto", articleDto);
        return "article/articleWrite";

    }

    @PostMapping(value = "/boardCreate")
    public String createArticle(@Valid ArticleDto articleDto, BindingResult result, Principal principal, Model model) {
        String email= principal.getName();
        try {
            articleService.saveAriticle(articleDto, email);
        }catch (Exception e){
            model.addAttribute(e.getStackTrace());
        }



        //redirect : 브라우저가 해당 URL로 재요청
        return "redirect:/article/list";
    }

    @GetMapping(value = "/{id}")
    public String show (@PathVariable Long id, Model model) {
        ArticleDto articleDto =  articleService.findArticle(id);
        // model에 선택한 글의 id를 가지고 글의 내용을 들고 있는 Dto 객체를 추가
        model.addAttribute("boardDto", articleDto);
        // 글의 id값을 가지고 있는 comment를 List로 하여 model에 추가
        List<CommentDto> commentDtoList = commentService.commentDtoList(id);
        model.addAttribute("commentList", commentDtoList);
        // 새로 입력받을 commentDto 객체를 넘겨준다.
        model.addAttribute("newCommentDto", new CommentDto());
        return "article/detail";
    }

//    @GetMapping(value = "/{id}/boardEdit")
//    public String edit(@PathVariable Long id, Model model) {
//        Board boardEntity = boardRepository.findById(id).orElse(null);
//        model.addAttribute("board",boardEntity);
//        return "board/boardEdit";
//    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        ArticleDto articleDto = articleService.findArticle(id);
        model.addAttribute("boardDto", articleDto);

        return "board/boardEdit";
    }

    @PostMapping(value = "/edit")
    public String update(@Valid ArticleDto articleDto, BindingResult result, Model model) {

        try{
            articleService.ArticleUpdate(articleDto);
        }catch (Exception e){
            model.addAttribute("errorMsg", result.getFieldError());
        }


        return "redirect:/article/list";
    }

    @GetMapping(value = "/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Article target = articleRepository.findById(id).orElse(null);

        if(target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("ArticleSuccessMsg", "삭제가 완료되었습니다.");
        }


        return "redirect:/article/list";
    }


}