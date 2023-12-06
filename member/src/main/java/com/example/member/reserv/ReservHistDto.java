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

    private String CheckIn;
    private String CheckOut;


// 주문 내역 조회 시 필요한 데이터
    // reservID,숙소네임,숙소 대표이미지, roomId,룸 네임 , memberId, reservName,
    // reservPN,방가격, 예약날짜, 상태, 체크인아웃, 인원(?),

    // room
    // 숙소네임,숙소 대표이미지, roomId,룸 네임,방가격,상태, 체크인아웃, 인원(?)

    // reserv
    // reservId, reservName, reservPN, 예약날짜

    // member
    // memberId







}
