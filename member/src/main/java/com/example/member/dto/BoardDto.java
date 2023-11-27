package com.example.member.dto;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardDto {

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

    public Board toEntity() {
        return new Board(id, boardTitle, content);
    }
}
