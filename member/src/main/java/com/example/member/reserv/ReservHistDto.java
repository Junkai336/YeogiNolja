package com.example.member.reserv;

import com.example.member.constant.ReservationStatus;
import com.example.member.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReservHistDto {

    private Long reservId; // 예약 아이디
    private Reserv reserv;
    // 예약 내역 리스트

}
