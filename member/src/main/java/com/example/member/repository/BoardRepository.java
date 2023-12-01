package com.example.member.repository;

import com.example.member.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    List<Board> findAll();


    @Query(value = "select * from board b where b.member_id= :member_id",nativeQuery = true)
    List<Board> findAllByMemberId(@Param("member_id")Long member_id);


}