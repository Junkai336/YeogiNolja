package com.example.member.article.comment;

import com.example.member.article.Article;
import com.example.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class CommentDto {

    private Long id;

    private String commentWriter;

    private String comment;

    private Long article_id;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setComment(comment.getComment());
        commentDto.setArticle_id(comment.getArticle().getId());
        commentDto.setRegTime(comment.getRegTime());
        commentDto.setUpdateTime(comment.getUpdateTime());
        return commentDto;
    }
}
