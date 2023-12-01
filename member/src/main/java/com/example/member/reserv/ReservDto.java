package com.example.member.reserv;

import com.example.member.entity.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReservDto {
    private Long id; // 예약 id

    private Long roomId; // 숙소 id

    //예약자 정보
    @NotEmpty(message = "성명을 입력해주세요")
    private String reservName; // 예약자 이름

    @NotEmpty(message = "휴대폰번호를 입력해주세요")
    private String reservPhone; // 예약자 전화번호


    public String phoneN(Member member){
        String number =member.getPhoneN1()+"-"+member.getPhoneN2()+"-"+member.getPhoneN3();
        return number;
    }

}
