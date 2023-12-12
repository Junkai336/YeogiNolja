package com.example.member.reserv.reservDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservedDateRepository extends JpaRepository<ReservedDate, Long> {

    @Query(value = "select * from rev_date_table rd where rd.room_id= :room_id", nativeQuery = true)
     List<ReservedDate> findByRoomId(@Param("room_id") Long room_id);

    @Query(value = "select rd.room_id from rev_date_table rd where rd.reserved_date= :check_date", nativeQuery = true)
    List<Long> findAllByDate(@Param("check_date") LocalDate date);


}
