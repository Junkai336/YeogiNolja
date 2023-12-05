package com.example.member.reserv;

import com.example.member.constant.ReservationStatus;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservs")
@Getter
@Setter
public class Reserv extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "reserv_id")
    private Long id; // 예약 id

    @ManyToOne(fetch = FetchType.LAZY) // 멤버가 주
    @JoinColumn(name = "member_id")
    private Member member;  // 오더 주체가 되는 Member

    @JoinColumn(name = "lodging_id")
    @ManyToOne(fetch = FetchType.LAZY) // 숙소가 주
    private Lodging lodging; // 숙소 id
    @Column
    private String lodgingName;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @Column
    private String reservName; // 예약자 이름
    @Column
    private String reservPN; // 예약자 전화번호

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus; // 예약 상태


    // 예약 생성
    public static Reserv createReserv(ReservDto reservDto){
        Reserv reserv = new Reserv();
        reserv.setRoom(reservDto.getRoom());
        reserv.setMember(reservDto.getMember());
        reserv.setReservName(reservDto.getReservName());
        reserv.setReservPN(reservDto.getReservPN());
        return reserv;
    }

    // 예약 취소
    public void cancelReserv() {
        this.reservationStatus = ReservationStatus.AVAILABLE;
    }

}
