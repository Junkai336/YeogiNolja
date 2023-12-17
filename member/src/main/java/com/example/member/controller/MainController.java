package com.example.member.controller;

import com.example.member.article.ArticleDto;
import com.example.member.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping(value = "/")
    public String toHome(Model model) {
        List<ArticleDto> articleDtoList = mainService.articles();

        model.addAttribute("articleDtoList",articleDtoList);
        return "index";
    }

}


//select title from article where category_status='NOTICE' and category_status='FREE' ORDER BY reg_time desc limit 5;
//select title from article where category_status='NOTICE' ORDER BY reg_time desc limit 5;
//select title from article where category_status='FREE' ORDER BY reg_time desc limit 5;

//select m from Member m where user_role='USER' order by id desc