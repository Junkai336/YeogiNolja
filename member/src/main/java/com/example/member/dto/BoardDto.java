package com.example.member.dto;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Board;
import com.example.member.entity.Member;
import lombok.*;

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
//    // 조회수
//    private Long views;
//
//    // 추천수
//    private Long recommendations;

    public static BoardDto toBoardDto(Board board){
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setContent(board.getContent());
        boardDto.setBoardCategoryStatus(board.getBoardCategoryStatus());
        boardDto.setMember(board.getMember());

        return boardDto;
    }

}