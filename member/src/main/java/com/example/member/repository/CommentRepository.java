package com.example.member.repository;

import com.example.member.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true, value = "select * from comment c where c.board_id= :board_id")
    List<Comment> findAllByBoardId(@Param("board_id") Long board_id);

}
