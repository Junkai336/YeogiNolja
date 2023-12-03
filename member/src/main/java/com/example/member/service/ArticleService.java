package com.example.member.service;

import com.example.member.dto.ArticleDto;
import com.example.member.entity.Article;
import com.example.member.entity.Member;
import com.example.member.repository.ArticleRepository;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

//    public Board create(BoardDto boardDto) {
//        Board board = boardDto.toEntity();
//        if(board.getId() != null){
//            return null;
//        } //기존데이터 수정 방지
//        return boardRepository.save(board);
//    }

    public List<ArticleDto> articleDtoList() throws Exception {
        List<Article> articleList = articleRepository.findAll();
        List<ArticleDto> articleDtoList =new ArrayList<>();

        for (Article article : articleList) {
            ArticleDto articleDto = ArticleDto.toArticleDto(article);
            articleDtoList.add(articleDto);
        }

        return articleDtoList;
    }

    public void saveAriticle(ArticleDto articleDto, String email)throws Exception{
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Article article = Article.toBoard(member, articleDto);
        articleRepository.save(article);
    }



    public List<ArticleDto> articleDtoList(Long member_id) throws Exception{

        List<Article> articleList = articleRepository.findAllByMemberId(member_id);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articleList){
            ArticleDto articleDto = ArticleDto.toArticleDto(article);
            articleDtoList.add(articleDto);
        }
        return articleDtoList;

    }


    public ArticleDto findArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return ArticleDto.toArticleDto(article);

    }



    public void ArticleUpdate(ArticleDto articleDto) {
        Article article = articleRepository.findById(articleDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());


    }
}