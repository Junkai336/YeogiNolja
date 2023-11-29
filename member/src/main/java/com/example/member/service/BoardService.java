package com.example.member.service;

import com.example.member.dto.BoardDto;
import com.example.member.entity.Board;
import com.example.member.entity.Member;
import com.example.member.repository.BoardRepository;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

//    public Board create(BoardDto boardDto) {
//        Board board = boardDto.toEntity();
//        if(board.getId() != null){
//            return null;
//        } //기존데이터 수정 방지
//        return boardRepository.save(board);
//    }

    public List<BoardDto> boardDtoList () {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDtoList =new ArrayList<>();

        for (Board board : boardList) {
            BoardDto boardDto = BoardDto.toBoardDto(board);
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }

    public void saveBoard(BoardDto boardDto, String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Board board = Board.toBoard(member, boardDto);
        boardRepository.save(board);
    }

//    public Board findId(Long id) {
//        Board boardEntityId = boardRepository.findById(id).orElseThrow(EntityNotFoundException:: new);
//
//        return boardEntityId;
//    }

//    public Board deleteBoard(Board target) {
//
//        if(target != null) {
//            boardRepository.delete(target);
//        }
//
//        return target;
//
//    }

    public List<BoardDto> boardDtos(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Long member_id = member.getId();
        List<Board> boardList = boardRepository.findAllByMemberId(member_id);
        List<BoardDto> boardDtoList = new ArrayList<>();
        for (Board board : boardList){
            BoardDto boardDto= BoardDto.toBoardDto(board);
            boardDtoList.add(boardDto);
        }
        return boardDtoList;

    }

}