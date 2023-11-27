package com.example.member.repository;

import com.example.member.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    ArrayList<Board> findAll();

}
