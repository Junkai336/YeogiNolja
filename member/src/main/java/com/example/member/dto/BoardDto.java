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
    private String writer; // 작성자
    private String boardTitle;

    private String content;

    private BoardCategoryStatus boardCategoryStatus;

    private Long member_id;
//    // 조회수
//    private Long views;
//
//    // 추천수
//    private Long recommendations;

    public static BoardDto toBoardDto(Board board){
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setWriter(board.getWriter());
        boardDto.setContent(board.getContent());
        boardDto.setBoardCategoryStatus(board.getBoardCategoryStatus());
        boardDto.setMember_id(board.getMember_id());

        return boardDto;
    }

}
