package com.example.member.reserv;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReservSaveDto {

    // 예약자명
    private String name;

    // 예약자 전화번호
    private String tel;

    // 숙소 아이디
    private Long lodging_id;

    // 객실 아이디
    private Long room_id;

    // 멤버 아이디
    private Long member_id;

    // 체크인 시간
    private String checkIn;

    // 체크아웃 시간
    private String checkOut;

}
