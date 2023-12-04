package com.example.member.article.comment;

import com.example.member.article.Article;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "comment_table")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String commentWriter;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public static Comment crateComment(CommentDto commentDto, Article article) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setArticle(article);
        return comment;
    }
}
