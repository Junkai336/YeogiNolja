package com.example.member.reserv;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservRepository extends JpaRepository<Reserv,Long> {



    // 페이징
    @Query(value = "select r from Reserv r "+
            "where r.member.email = :email "+
            "order by r.regTime desc")
    List<Reserv> findReservs(@Param("email") String email, Pageable pageable);
    // 현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞추어 조회함
    // count(r) : 조건식을 만족하는 Reserv r 의 수를 리턴한다.
    @Query(value = "select count(r) from Reserv r where r.member.email = :email")
    Long countReserv(@Param("email") String email);

    @Query(value = "select * from Reserv r where r.member.id= :member_id", nativeQuery = true)
    List<Reserv> findAllByMemberId(@Param("member_id") Long memberId);

    // 현재 로그인한 회원의 예약 개수가 몇 개인지 조회


}
