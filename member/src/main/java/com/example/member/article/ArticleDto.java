package com.example.member.article;

import com.example.member.article.Article;
import com.example.member.article.comment.CommentDto;
import com.example.member.constant.CategoryStatus;
import com.example.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.member.article.comment.CommentDto.toCommentDtoList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArticleDto{

    private Long id;

    private String title;

    private String content;

    private CategoryStatus categoryStatus;

    private String articleStatus;

    private Member member;

    private String createdBy;

    private LocalDateTime regTime;

    /*메인에 출력할 String Date*/
    private String regDateStr;

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
        // 리스트 분류에 한글로 표기
        if (articleDto.getCategoryStatus() == CategoryStatus.NOTICE) {
            articleDto.setArticleStatus("공지사항");
        } else if (articleDto.getCategoryStatus() == CategoryStatus.FREE) {
            articleDto.setArticleStatus("자유게시판");
        } else if (articleDto.getCategoryStatus() == CategoryStatus.EVENT) {
            articleDto.setArticleStatus("이벤트");
        }
        return articleDto;
    }


}