package com.example.member.repository;

import com.example.member.entity.Comment;
import com.example.member.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository  extends JpaRepository<Room, Long> {

    @Query(nativeQuery = true, value = "select * from Room r where r.lodging_id= :lodging_id order by reg_time desc")
    List<Room> findAllByLodgingId(@Param("lodging_id") Long lodging_id);

}
