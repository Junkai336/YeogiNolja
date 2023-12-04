package com.example.member.dto;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.entity.Article;
import com.example.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.member.dto.CommentDto.toCommentDtoList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArticleDto {

    private Long id;

    private String title;

    private String content;

    private BoardCategoryStatus CategoryStatus;

    private Member member;

    private String createdBy;

    private LocalDateTime regTime;

    private List<CommentDto> commentDtoList = new ArrayList<>();
//    // 조회수
//    private Long views;
//
//    // 추천수
//    private Long recommendations;

    public static ArticleDto toArticleDto(Article article){
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setContent(article.getContent());
        articleDto.setCategoryStatus(article.getCategoryStatus());
        articleDto.setCommentDtoList(toCommentDtoList(article.getCommentList()));
        articleDto.setMember(article.getMember());
        articleDto.setCreatedBy(article.getCreatedBy());
        articleDto.setRegTime(article.getRegTime());
        return articleDto;
    }


}