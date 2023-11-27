package com.example.member.dto;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Board;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardDto extends BaseEntity {

    private Long id;

    private String boardTitle;

    private String content;

//    private String author;
//
//    private BoardCategoryStatus boardCategoryStatus;
//
//    // 등록일
//    private String regTime;
//
//    // 조회수
//    private Long views;
//
//    // 추천수
//    private Long recommendations;


}
