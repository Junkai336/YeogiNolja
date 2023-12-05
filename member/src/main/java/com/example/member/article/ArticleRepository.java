package com.example.member.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Override
    List<Article> findAll();


    @Query(value = "select * from board b where b.member_id= :member_id",nativeQuery = true)
    List<Article> findAllByMemberId(@Param("member_id")Long member_id);


}