package com.example.member.dto;

import com.example.member.constant.LodgingType;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LodgingDto {

    private Long id;

    private Room room;

    private Member member;

//    @Column
//    private Review review;

    private String name;

    private String detail;

    private String price;

    private String location;

    private LodgingType lodgingType;

    private String createdBy;

    private String modifiedBy;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    // Entity -> Dto
    public static LodgingDto toLodgingDto (Lodging lodging) {
        LodgingDto lodgingDto = new LodgingDto();
        lodgingDto.setId(lodging.getId());
//        lodgingDto.setRoom(lodging.getRoom());
        lodgingDto.setMember(lodging.getMember());
        lodgingDto.setName(lodging.getName());
        lodgingDto.setDetail(lodging.getDetail());
        lodgingDto.setPrice(lodging.getPrice());
        lodgingDto.setLocation(lodging.getLocation());
        lodgingDto.setLodgingType(lodging.getLodgingType());
        lodgingDto.setCreatedBy(lodging.getCreatedBy());
        lodgingDto.setModifiedBy(lodging.getModifiedBy());
        lodgingDto.setRegTime(lodging.getRegTime());
        lodgingDto.setUpdateTime(lodging.getUpdateTime());

        return lodgingDto;
    }

}
