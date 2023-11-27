package com.example.member.entity;

import com.example.member.constant.BoardCategoryStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private BoardCategoryStatus boardCategoryStatus;

    private String boardTitle;

    private String author;

    // 조회수
    private Long views;

    // 추천수
    private Long recommendations;

}
