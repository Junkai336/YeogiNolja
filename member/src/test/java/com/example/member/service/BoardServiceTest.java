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

        BoardDto boardDto = new BoardDto();

        boardDto.setContent("content");
        boardDto.setBoardTitle("title");

        boardService.saveBoard(boardDto);
    }
}