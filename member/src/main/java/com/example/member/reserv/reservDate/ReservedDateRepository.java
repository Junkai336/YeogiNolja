package com.example.member.reserv.reservDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservedDateRepository extends JpaRepository<ReservedDate, Long> {

    @Query(value = "select * from reserve_date rd where rd.room_id= :room_id", nativeQuery = true)
     List<ReservedDate> findByRoomId(@Param("room_id") Long room_id);

    @Query(value = "SELECT room_id FROM just_board.reserve_date where reserve_date=:date", nativeQuery = true)
    List<Long> findAllByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT room_id FROM just_board.reserve_date where reserve_date=:date and room_id=:room_id", nativeQuery = true)
    List<Long> findByDateAndRoom(@Param("date") LocalDate date,@Param("room_id") Long roomId);
}
