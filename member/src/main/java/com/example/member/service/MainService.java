package com.example.member.service;

import com.example.member.article.Article;
import com.example.member.article.ArticleDto;
import com.example.member.article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final ArticleRepository articleRepository;
    public List<ArticleDto> articles() {
        List<Article> articleList1 = articleRepository.findArticlesNotice();
        List<Article> articleList2 = articleRepository.findArticlesFree();
        List<ArticleDto> articleDtoList = new ArrayList<>();

        for(Article article : articleList1){
            ArticleDto articleDto = ArticleDto.toArticleDto(article);
            String mainDate = localDateToString(articleDto);
            articleDto.setRegDateStr(mainDate);
            articleDtoList.add(articleDto);
        }
        for(Article article : articleList2){
            ArticleDto articleDto = ArticleDto.toArticleDto(article);
            String mainDate = localDateToString(articleDto);
            articleDto.setRegDateStr(mainDate);
            articleDtoList.add(articleDto);
        }
        return articleDtoList;
    }

    public String localDateToString(ArticleDto articleDto){
        LocalDateTime regTime = articleDto.getRegTime();
        String regTimeStr = regTime.toString();
        String[] dateStr = regTimeStr.split("T");
        String mainDate = dateStr[0].replace("-", ".");
        return mainDate;
    }
}
