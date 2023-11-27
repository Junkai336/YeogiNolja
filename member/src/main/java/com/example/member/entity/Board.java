package com.example.member.entity;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.dto.BoardDto;
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
@Setter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String boardTitle;

    private String content;

//    private String author;
//
//    private BoardCategoryStatus boardCategoryStatus;
//
//    // 조회수
//    private Long views;
//
//    // 추천수
//    private Long recommendations;

    public static Board toBoard(BoardDto boardDto){
        Board board =new Board();
        board.setBoardTitle(boardDto.getBoardTitle());
        board.setContent(boardDto.getContent());
        return board;
    }

}
