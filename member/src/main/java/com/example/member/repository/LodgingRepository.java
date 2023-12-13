package com.example.member.repository;

import com.example.member.article.Article;
import com.example.member.entity.Lodging;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
//
//public interface LodgingRepository extends JpaRepository<Lodging, Long>,
//        QuerydslPredicateExecutor<Lodging>, LodgingRepositoryCustom {

import com.example.member.entity.Lodging;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LodgingRepository extends JpaRepository<Lodging, Long> {

    @Query(value = "select l from Lodging l order by id desc")
    List<Lodging> findLodgings(Pageable pageable);

    @Query("select count(l) from Lodging l")
    Long countLodging();
}
