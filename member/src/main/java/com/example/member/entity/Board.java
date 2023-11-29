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
    private Long id;


    @Column
    private String boardTitle;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardCategoryStatus boardCategoryStatus;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

//
//    // 조회수
//    @Column
//    private Long views;
//
//    // 추천수
//    @Column
//    private Long recommendations;

    public static Board toBoard(Member member, BoardDto boardDto){

        Board board = new Board();
        board.setBoardTitle(boardDto.getBoardTitle());
        board.setContent(boardDto.getContent());
        board.setMember(member);
        board.setBoardCategoryStatus(boardDto.getBoardCategoryStatus());
        return board;
    }

}