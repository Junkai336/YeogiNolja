package com.example.member.reserv.reservDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedDateRepository extends JpaRepository<ReservedDate, Long> {

    @Query(value = "select * from rev_date_table rd where rd.room_id= :room_id", nativeQuery = true)
     List<ReservedDate> findByRoomId(@Param("room_id") Long room_id);
}
