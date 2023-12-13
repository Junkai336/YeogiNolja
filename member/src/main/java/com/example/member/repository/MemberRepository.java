package com.example.member.repository;

import com.example.member.article.Article;
import com.example.member.constant.UserRole;
import com.example.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    @Override
    List<Member> findAll();

    //    nativeQuery=true : sql문법 사용
    @Query(value = "select * from member where member.user_role='USER'", nativeQuery = true)
    List<Member> findAllByUser();


    @Query(value = "select m from Member m where member.user_role='USER' order by id desc",nativeQuery = true)
    List<Member> findMembersUserForPaging(Pageable pageable);

    @Query(value = "select count(m) from Member m where member.user_role='USER'",nativeQuery = true)
    
    Long countMemberUser();
}