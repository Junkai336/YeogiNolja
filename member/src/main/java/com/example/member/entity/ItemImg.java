package com.example.member.entity;

import com.example.member.dto.ItemImgDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemImg {
    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id")
    private Lodging lodging;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void updateItemImg(String oriImgName,
                              String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
    //원본이미지파일명,업데이트할 이미지파일명 , 이미지경로를 파라미터로
    //입력받아
    //이미지 정보를 업데이트 하는 메소드

    // Entity -> Dto
    public static ItemImg toItemImg (ItemImgDto itemImgDto) {

        ItemImg itemImg = new ItemImg();
        itemImg.setImgUrl(itemImgDto.getImgUrl());
        itemImg.setOriImgName(itemImgDto.getOriImgName());
        itemImg.setImgName(itemImgDto.getImgName());
        itemImg.setRepimgYn(itemImgDto.getRepimgYn());
        itemImg.setLodging(itemImgDto.getLodging());
        itemImg.setRoom(itemImgDto.getRoom());

        return itemImg;
    }
}
