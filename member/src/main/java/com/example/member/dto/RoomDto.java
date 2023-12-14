package com.example.member.dto;

import com.example.member.constant.ReservationStatus;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Lodging;
import com.example.member.entity.Room;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoomDto {

    private Long id;

    private Lodging lodging;

    private ReservationStatus reservationStatus;

    private String name;

    private String detail;

    private String adult;

    private String children;

    private String price;

    private String checkInTime;

    private String checkOutTime;

    private String checkIn;

    private String checkOut;

    private Long lodgingId;

    private String imgUrl;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    public static RoomDto toRoomDto (Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setLodging(room.getLodging());
        roomDto.setReservationStatus(room.getReservationStatus());
        roomDto.setName(room.getName());
        roomDto.setPrice(room.getPrice());
        roomDto.setAdult(room.getAdult());
        roomDto.setChildren(room.getChildren());
        roomDto.setDetail(room.getDetail());
        roomDto.setCheckInTime(room.getCheckInTime());
        roomDto.setCheckOutTime(room.getCheckOutTime());

        return roomDto;
    }

    public static List<RoomDto> toRoomDtoList(List<Room> roomList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (Room room : roomList) {
            RoomDto roomDto = toRoomDto(room);
            roomDtoList.add(roomDto);
        }
        return roomDtoList;

    }



}
