package com.example.member.reserv;

import com.example.member.lodging.Lodging;
import com.example.member.entity.Member;
import com.example.member.lodging.Room;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReservDto {

    private Long lodgingId; // 숙소 id

    //예약자 정보
    @NotEmpty(message = "성명을 입력해주세요")
    private String reservName; // 예약자 이름

    @NotEmpty(message = "휴대폰번호를 입력해주세요")
    private String reservPhoneN1; // 예약자 전화번호

}
