package com.example.member.article;

import com.example.member.article.comment.Comment;
import com.example.member.constant.CategoryStatus;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Member;
import groovy.util.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "article")
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "article_id")
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private CategoryStatus categoryStatus;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private Long views; // 조회 수

    @Column
    private Long recommendations; // 추천 수


    public static Article createArticle(ArticleDto articleDto, Member member) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setMember(member);
        article.setCategoryStatus(CategoryStatus.NOTICE);
        return article;
    }
}
