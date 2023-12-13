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
@ToString
@NoArgsConstructor
public class ReservDto {
    private Long id; // 예약 id

    private Member member;

    private Room room;

    private Lodging lodging;

    //예약자 정보
    @NotEmpty(message = "성명을 입력해주세요")
    private String reservName; // 예약자 이름

    @NotEmpty(message = "휴대폰번호를 입력해주세요")
    private String reservPN; // 예약자 전화번호

    private LocalDateTime reservDate; // 예약일

    private ReservationStatus reservationStatus;

    private String checkIn;
    private String checkOut;

    public static ReservDto toReservDto(Reserv reserv){
        Member member = reserv.getMember();
        ReservDto reservDto = new ReservDto();
        reservDto.setId(reserv.getId());
        reservDto.setMember(member);
        reservDto.setReservPN(reservDto.phoneNumber(member));
        reservDto.setRoom(reserv.getRoom());
        reservDto.setReservDate(reserv.getRegTime());
        reservDto.setReservName(reserv.getReservName());
        reservDto.setReservPN(reserv.getReservPN());
        reservDto.setReservationStatus(reserv.getReservationStatus());
        reservDto.setCheckIn(reserv.getCheckIn());
        reservDto.setCheckOut(reserv.getCheckOut());


        return reservDto;
    }
    public static String phoneNumber(Member member){
        String number=
                member.getPhoneN1()+"-"+
                        member.getPhoneN2()+"-"+
                        member.getPhoneN3();
        return number;
    }

}
