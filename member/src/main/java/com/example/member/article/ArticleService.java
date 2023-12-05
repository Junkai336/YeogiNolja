package com.example.member.article;

import com.example.member.entity.Member;
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

    public List<ArticleDto> articleDtoList () {
        List<Article> articleList = articleRepository.findAll();
        List<ArticleDto> articleDtoList =new ArrayList<>();

        for (Article article : articleList) {
            ArticleDto articleDto = ArticleDto.toArticleDto(article);
            articleDtoList.add(articleDto);
        }

        return articleDtoList;
    }

    public void saveArticle(ArticleDto articleDto, String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Article article = Article.toArticle(member, articleDto);
        articleRepository.save(article);
    }



    public List<ArticleDto> articleDtoList(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Long member_id = member.getId();
        List<Article> articleList = articleRepository.findAllByMemberId(member_id);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articleList){
            ArticleDto articleDto= ArticleDto.toArticleDto(article);
            articleDtoList.add(articleDto);
        }
        return articleDtoList;

    }


    public ArticleDto findArticle(Long id) {
        Article article = articleRepository.findById(id).orElse(null);

        return ArticleDto.toArticleDto(article);

    }



    public void articleUpdate(ArticleDto articleDto) {
        Article article = articleRepository.findById(articleDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());

    }
    public void articleDelete(Long article_id)throws Exception{
        Article article = articleRepository.findById(article_id)
                .orElseThrow(EntityNotFoundException::new);
        articleRepository.delete(article);
    }



}