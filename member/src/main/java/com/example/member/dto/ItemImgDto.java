package com.example.member.dto;

import com.example.member.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

//    private static ModelMapper modelMapper = new ModelMapper();

        //of 변환메서드에서사용
    // *************************** ↓ 주석처리 했음 *******************************
//    public static ItemImgDto of(ItemImg itemImg) {
//        return modelMapper.map(itemImg,ItemImgDto.class);
//    }
    // *************************************************************************
    //map 소스메소드 itemImg 소스객체를  대상클래스(ItemImgDto.class)를 인자로 받아서 변환 수행
    // static 메소드로 선언해서 ItemImgDto를 생성하지 않아도 호출 할 수있도록 한다.

//ModelMapper를 사용하여 ItemImg 엔티티를 ItemImgDto로 변환하는 정적메서드



    // public ItemImgDto() {
    // 기본 생성자
    // }

//    public ItemImgDto(ItemImg itemImg) {
//        this.id = itemImg.getId();
//        this.imgName = itemImg.getImgName();
//        this.oriImgName = itemImg.getOriImgName();
//        this.imgUrl = itemImg.getImgUrl();
//        this.repImgYn = itemImg.getRepImgYn();
//    }
}

