package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
    // @QueryProjection 을 사용하면 Item 객체로 값은 받은후 DtO 클래스로
    //변환하는 과정없이 바로 DTO 객체를 뽑아낼수 있다.

}
