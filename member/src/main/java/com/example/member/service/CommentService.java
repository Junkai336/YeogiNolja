package com.example.member.service;

import com.example.member.dto.BoardDto;
import com.example.member.dto.CommentDto;
import com.example.member.entity.Board;
import com.example.member.entity.Comment;
import com.example.member.entity.Member;
import com.example.member.repository.BoardRepository;
import com.example.member.repository.CommentRepository;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public void newComment(CommentDto commentDto, String email, Long boardId) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        Comment comment = Comment.toComment(commentDto, member, board);
        try{
            commentRepository.save(comment);
        }catch (Exception e){
            e.getStackTrace();
        }

    }

    public List<CommentDto> commentDtoList(Long board_id){
        List<Comment> commentList = commentRepository.findAllByBoardId(board_id);
        List<CommentDto> commentDtoList = CommentDto.toCommentDtoList(commentList);
        return commentDtoList;

    }







}
