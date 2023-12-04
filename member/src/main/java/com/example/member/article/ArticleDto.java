package com.example.member.article;

import com.example.member.constant.CategoryStatus;
import com.example.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {

    private Long id;

    private String title;

    private String content;

    private CategoryStatus categoryStatus;

    private Member member;

    private Long views; // 조회 수

    private Long recommendations; // 추천 수

    private LocalDateTime regTime; // 생성일자

    public static ArticleDto toArticleDto(Article article) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setContent(article.getContent());
        articleDto.setMember(article.getMember());
        articleDto.setCategoryStatus(article.getCategoryStatus());
        articleDto.setViews(article.getViews());
        articleDto.setRecommendations(article.getRecommendations());
        return articleDto;
    }
}
