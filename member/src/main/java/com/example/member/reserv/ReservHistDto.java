package com.example.member.reserv;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReservHistDto {
    public ReservHistDto(Reserv reserv){
        this.orderId = reserv.getId();
    }

    private Long orderId; // 주문 아이디
    private String orderDate; // 주문 날짜
    private ReservStatus reservStatus; // 주문 상태

    // 주문 상품 리스트
    private List<ReservItemDto> reservItemDtoList = new ArrayList<>();

    public void addOrderItemDto(ReservItemDto reservItemDto){
        reservItemDtoList.add(reservItemDto); // orderItemDto를 리스트로 추가
    }
}
