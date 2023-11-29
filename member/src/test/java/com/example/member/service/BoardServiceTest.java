package com.example.member.service;

import com.example.member.dto.BoardDto;
import com.example.member.entity.Board;
import com.example.member.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    public void saveBoard() {

//        Board board = Board.builder()
//                .content("content")
//                .boardTitle("title")
//                .build();
//
//        boardRepository.save(board);

        BoardDto boardDto = new BoardDto();

        boardDto.setContent("content");
        boardDto.setBoardTitle("title");

        Board board = Board.builder()
//                 .id(1L)
                .content(boardDto.getContent())
                .boardTitle(boardDto.getBoardTitle())
                .build();

        boardRepository.save(board);

//        boardService.saveBoard(boardDto);

    }
}