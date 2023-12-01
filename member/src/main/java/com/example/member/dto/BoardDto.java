package com.example.member.dto;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Board;
import com.example.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardDto{

    private Long id;

    private String boardTitle;

    private String content;

    private BoardCategoryStatus boardCategoryStatus;

    private Member member;

    private String createdBy;

    private LocalDateTime regTime;

//    // 조회수
//    private Long views;
//
//    // 추천수
//    private Long recommendations;

    public static BoardDto toBoardDto(Board board){
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setBoardTitle(board.getBoardTitle());
        boardDto.setContent(board.getContent());
        boardDto.setBoardCategoryStatus(board.getBoardCategoryStatus());
        boardDto.setMember(board.getMember());
        boardDto.setCreatedBy(board.getCreatedBy());
        boardDto.setRegTime(board.getRegTime());


        return boardDto;
    }

}