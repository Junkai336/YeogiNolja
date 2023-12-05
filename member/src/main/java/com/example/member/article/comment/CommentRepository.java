package com.example.member.article.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comment c where c.article_id = :article_id", nativeQuery = true)
    List<Comment> findAllByArticleId(@Param("article_id") Long article_id);


}
