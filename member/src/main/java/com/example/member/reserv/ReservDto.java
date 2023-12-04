package com.example.member.reserv;

import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReservDto {
    private Long id; // 예약 id

    @Column
    private Member member;

    @Column
    private Lodging lodging;

    //private String lodgingName;

    @Column
    private Room room;

    //private String roomName;

    //private String roomDetail;

    //private String roomCheckInTime;
    //private String roomCheckOutTime;

    //private String roomPrice;

    //예약자 정보
    @NotEmpty(message = "성명을 입력해주세요")
    private String reservName; // 예약자 이름

    @NotEmpty(message = "휴대폰번호를 입력해주세요")
    private String reservPN; // 예약자 전화번호


}
