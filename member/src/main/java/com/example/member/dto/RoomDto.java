package com.example.member.dto;

import com.example.member.constant.ReservationStatus;
import lombok.*;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoomDto {

    private Long id;

    private ReservationStatus reservationStatus;

    private String name;

    private String detail;

    private String price;

    private String checkInTime;

    private String checkOutTime;
}
