package com.example.member.dto;

import com.example.member.constant.LodgingType;
import com.example.member.entity.Room;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

public class LodgingDto {

    private Long id;

    private Room room;

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

}
