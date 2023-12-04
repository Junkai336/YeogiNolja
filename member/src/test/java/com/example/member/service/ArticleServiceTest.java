package com.example.member.service;

import com.example.member.dto.ArticleDto;
import com.example.member.entity.Article;
import com.example.member.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Test
    public void saveBoard() {

//        Board board = Board.builder()
//                .content("content")
//                .boardTitle("title")
//                .build();
//
//        boardRepository.save(board);

        ArticleDto articleDto = new ArticleDto();

        articleDto.setContent("content");
        articleDto.setTitle("title");

        Article article = Article.builder()
//                 .id(1L)
                .content(articleDto.getContent())
                .title(articleDto.getTitle())
                .build();

        articleRepository.save(article);

//        boardService.saveBoard(boardDto);

    }
}