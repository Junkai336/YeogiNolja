package com.example.member.dto;

import com.example.member.constant.BoardCategoryStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {

    private Long id;

    private BoardCategoryStatus boardCategoryStatus;

    private String boardTitle;

    private String author;

    // 등록일
    private String regTime;

    // 조회수
    private Long views;

    // 추천수
    private Long recommendations;

}
