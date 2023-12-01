package com.example.member.reservItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservItemDto {
    private String lodgingName; // 숙소명
    private String lodgingPrice; // 숙소 가격
    private String imgUrl; // 숙소 이미지 경로

    public ReservItemDto(ReservItem reservItem, String imgUrl) {
        this.lodgingName = reservItem.getLodging().getName();
        this.lodgingPrice = reservItem.getReservPrice();
        this.imgUrl = imgUrl;
    }
    // 예약 숙소 정보를 담을 Dto
    // 예약 항목에 대한 데이터 전달 및 표현을 위해 사용
}
