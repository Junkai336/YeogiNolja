package com.example.member.reserv;

import com.example.member.constant.ReservationStatus;
import com.example.member.reservItem.ReservItemDto;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReservHistDto {

    private Long reservId; // 주문 아이디
    private String reservDate; // 주문 날짜
    private ReservationStatus reservationStatus; // 주문 상태
    // 주문 상품 리스트
    private List<ReservItemDto> reservItemDtoList = new ArrayList<>();

    public void addReservItemDto(ReservItemDto reservItemDto){
        reservItemDtoList.add(reservItemDto); // orderItemDto를 리스트로 추가
    }

    public ReservHistDto(Reserv reserv){
        this.reservId = reserv.getId();
        this.reservDate = reserv.getReservDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.reservationStatus = reserv.getReservationStatus();
    }
}
