package com.example.member.reserv;

import com.example.member.constant.ReservationStatus;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservDto {
    private Long id; // 예약 id

    private Member member;

    private Room room;

    //예약자 정보
    @NotEmpty(message = "성명을 입력해주세요")
    private String reservName; // 예약자 이름

    @NotEmpty(message = "휴대폰번호를 입력해주세요")
    private String reservPN; // 예약자 전화번호

    private LocalDateTime reservDate; // 예약일

    public static ReservDto toReservDto(Reserv reserv){
        Member member = reserv.getMember();
        ReservDto reservDto = new ReservDto();
        reservDto.setMember(member);
        reservDto.setReservPN(reservDto.phoneNumber(member));
        reservDto.setRoom(reserv.getRoom());
        reservDto.setReservDate(reserv.getRegTime());
        reservDto.setReservName(reserv.getReservName());
        reservDto.setReservPN(reserv.getReservPN());

        return reservDto;
    }
    public static String phoneNumber(Member member){
        String number=
                member.getPhoneN1()+"-"+
                        member.getPhoneN2()+"-"+
                        member.getPhoneN3();
        return number;
    }
    
    // 주문 내역 조회 시 필요한 데이터
    // reservID,숙소네임,숙소 대표이미지, roomId,룸 네임 , memberId, reservName,
    // reservPN,방가격, 예약날짜, 상태, 체크인아웃, 인원(?),

    // room
    // 숙소네임,숙소 대표이미지, roomId,룸 네임,방가격,상태, 체크인아웃, 인원(?)

    // reserv
    // reservId, reservName, reservPN, 예약날짜

    // member
    // memberId


}
