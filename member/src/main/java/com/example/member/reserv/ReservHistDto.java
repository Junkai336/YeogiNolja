package com.example.member.reserv;

import com.example.member.constant.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReservHistDto {

    private Long reservId; // 예약 아이디
    private String reservDate; // 예약 날짜
    private ReservationStatus reservationStatus; // 예약 상태
    // 예약 내역 리스트
    private List<ReservDto> reservDtoList = new ArrayList<>();

// 주문 내역 조회 시 필요한 데이터
    // reservID,숙소네임,숙소 대표이미지, roomId,룸 네임 , memberId, reservName,
    // reservPN,방가격, 예약날짜, 상태, 체크인아웃, 인원(?),

    // room
    // 숙소네임,숙소 대표이미지, roomId,룸 네임,방가격,상태, 체크인아웃, 인원(?)

    // reserv
    // reservId, reservName, reservPN, 예약날짜

    // member
    // memberId

    public void addReservDto(ReservDto reservDto){
        reservDtoList.add(reservDto); // reservDto를 리스트로 추가
    }

    public ReservHistDto(Reserv reserv){
        this.reservId = reserv.getId();
        this.reservDate = reserv.getRegTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.reservationStatus = reserv.getReservationStatus();
    }
}
