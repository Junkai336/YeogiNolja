package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;
        
    private String searchQuery = "";
}
//1. 현재시간과 상품 등록일을 비교해서 상품 데이터 조회
//all  상품등록일 전체
//1d 최근하루
//1w 최근 일주일
//1m 최근한달  6m
//2. 상품 판매상태
//3. 상품을 조회할때 어떠 유형으로 조회할지 선택합니다.
//itemNm : 상품명   createBy : 상품등록
//4. searchQuery 조회할 검색어 저장 변수
//searchBy - itemNm 상품명
// searchBy - createBy 상품등록자 아이디 기준











