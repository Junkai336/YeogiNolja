package com.example.member.entity;

import com.example.member.constant.ReservationStatus;
import com.example.member.dto.RoomDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="room")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="room_id")
    private Long id;

    @JoinColumn(name = "lodging_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lodging lodging;

//    @JoinColumn(name = "member_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member member;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column
    private String name;

    @Column
    private String detail;

    @Column
    private String price;

    @Column
    private String checkInTime;

    @Column
    private String checkOutTime;

//    @JoinColumn
//    private List<ItemImg> itemImgList = new ArrayList<>();


    public static Room toRoom(RoomDto roomDto, Lodging lodging) {
        Room room = new Room();
        room.setId(roomDto.getId());
        room.setLodging(lodging);
        room.setReservationStatus(roomDto.getReservationStatus());
        room.setName(roomDto.getName());
        room.setDetail(roomDto.getDetail());
        room.setPrice(roomDto.getPrice());
        room.setCheckInTime(roomDto.getCheckInTime());
        room.setCheckOutTime(roomDto.getCheckOutTime());

        return room;
    }

    // Entity 병합용

}
