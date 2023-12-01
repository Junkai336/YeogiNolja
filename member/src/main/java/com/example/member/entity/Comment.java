package com.example.member.entity;

import com.example.member.dto.CommentDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "comment")
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String comment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Comment toComment(CommentDto commentDto, Member member, Board board) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setMember(member);
        comment.setBoard(board);
        return comment;
    }
}
