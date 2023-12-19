package com.example.member.dto;

import com.example.member.constant.LodgingType;
import com.example.member.constant.RoomExist;
import com.example.member.entity.ItemImg;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LodgingDto {

    private Long id;

    private Room room;

    private List<Room> roomList = new ArrayList<>();

    private Member member;

//    @Column
//    private Review review;

    private String name;

    private String detail;

    private String postcode;   // 우편 번호

    private String address;     // 주소

    private String detailAddress; // 상세주소

    private String extraAddress; // 참고항목

    private LodgingType lodgingType;

    private RoomExist roomExist;

    private String createdBy;

    private String modifiedBy;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private String imgUrl;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private String minPrice;
    // Entity -> Dto
    public static LodgingDto toLodgingDto (Lodging lodging) {
        Member member = lodging.getMember();

        LodgingDto lodgingDto = new LodgingDto();
        lodgingDto.setId(lodging.getId());
        lodgingDto.setRoomList(lodging.getRoom());
        lodgingDto.setMember(member);
        lodgingDto.setName(lodging.getName());
        lodgingDto.setDetail(lodging.getDetail());
        lodgingDto.setPostcode(lodging.getPostcode());
        lodgingDto.setAddress(lodging.getAddress());
        lodgingDto.setRoomExist(lodging.getRoomExist());
        lodgingDto.setDetailAddress(lodging.getDetailAddress());
        lodgingDto.setExtraAddress(lodging.getExtraAddress());
        lodgingDto.setLodgingType(lodging.getLodgingType());
        lodgingDto.setCreatedBy(lodging.getCreatedBy());
        lodgingDto.setModifiedBy(lodging.getModifiedBy());
        lodgingDto.setRegTime(lodging.getRegTime());
        lodgingDto.setUpdateTime(lodging.getUpdateTime());

        return lodgingDto;
    }



}
