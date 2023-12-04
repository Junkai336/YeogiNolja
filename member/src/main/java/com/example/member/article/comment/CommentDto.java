package com.example.member.article.comment;

import com.example.member.article.Article;
import com.example.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private Member member;

    private Article article;

    private String comment;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
