package com.example.member.service;

import com.example.member.dto.BoardDto;
import com.example.member.entity.Board;
import com.example.member.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

//    public Board create(BoardDto boardDto) {
//        Board board = boardDto.toEntity();
//        if(board.getId() != null){
//            return null;
//        } //기존데이터 수정 방지
//        return boardRepository.save(board);
//    }


    public void saveBoard(BoardDto boardDto){
        Board board = Board.toBoard(boardDto);
        boardRepository.save(board);
    }

}
