package com.example.member.entity;

import com.example.member.constant.BoardCategoryStatus;
import com.example.member.dto.BoardDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="board_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String boardTitle;

    @Column
    private String content;

//    @Column
//    private String author;
//
//    @Enumerated(EnumType.STRING)
//    private BoardCategoryStatus boardCategoryStatus;
//
//    // 조회수
//    @Column
//    private Long views;
//
//    // 추천수
//    @Column
//    private Long recommendations;

    public static Board toBoard(BoardDto boardDto){
        Board board = new Board();
        board.setBoardTitle(boardDto.getBoardTitle());
        board.setContent(boardDto.getContent());
        return board;
    }

}
