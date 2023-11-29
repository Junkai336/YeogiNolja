package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;
    private List<CartOrderDto> cartOrderDtoList;

    // 장바구니에 여러 개의 상품을 주문하므로 CartOrderDto 클래스가 자기 자신을 리스트로 가지고 있도록 만듭니다.

}
