package com.example.member.entity;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.dto.ArticleDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="board_id")
    private Long id;


    @Column
    private String title;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardCategoryStatus CategoryStatus;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
//@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
//    private List<OrderItem> orderItems = new ArrayList<>();
//    // 외래키가(order_id)가 order_item 테이블에 있으므로
//    // 연관관계의 주인은 OrderItem
//    // OrderItem에 있는 Order에 의해 관리된다는 의미
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> commentList =new ArrayList<>();
//
//    // 조회수
//    @Column
//    private Long views;
//
//    // 추천수
//    @Column
//    private Long recommendations;

    public static Article toBoard(Member member, ArticleDto articleDto){

        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setMember(member);
        article.setCategoryStatus(articleDto.getCategoryStatus());
        return article;
    }


}