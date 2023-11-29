package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String itemNm; // 상품명
    private int count; // 주문 수량
    private int orderPrice; // 주문 금액
    private String imgUrl; // 상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
    // 주문 상품 정보를 담을 Dto
    // 주문 항목에 대한 데이터 전달 및 표현을 위해 사용

}
