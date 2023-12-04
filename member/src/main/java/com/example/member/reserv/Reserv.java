package com.example.member.reserv;

import com.example.member.constant.ReservationStatus;
import com.example.member.entity.BaseEntity;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.reservItem.ReservItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.Principal;
import java.time.LocalDateTime;
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
    private String roomName;

    @Column
    private String roomDetail;
    @Column
    private String roomCheckInTime;
    @Column
    private String roomCheckOutTime;
    @Column
    private String roomPrice;
    @Column
    private String checkInTime;
    @Column
    private String checkOutTime;
    @Column
    private String reservName; // 예약자 이름
    @Column
    private String reservPN; // 예약자 전화번호

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus; // 예약 상태
    @Column
    private LocalDateTime reservDate; // 예약일


    @OneToMany(mappedBy = "reserv", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<ReservItem> reservItems = new ArrayList<>();
    // 외래키가(reserv_id)가 rserv_item 테이블에 있으므로
    // 연관관계의 주인은 ReservItem
    // ReservItem에 있는 Reserv에 의해 관리된다는 의미

    public void addReservItem(ReservItem reservItem){
        reservItems.add(reservItem); // reservItem 객체를 reserv 객체에 reservItems 추가
        reservItem.setReserv(this); // 양방향 연관관계이므로 reservItem 객체 reserv 객체를 셋팅
    }

    // 예약 생성
    public static Reserv createReserv(ReservDto reservDto){
        Reserv reserv = new Reserv();
        reserv.setLodging(reservDto.getLodging());
        reserv.setLodgingName(reservDto.getLodging().getName());
        reserv.setRoom(reservDto.getRoom());
        reserv.setRoomName(reservDto.getRoom().getName());
        reserv.setRoomDetail(reservDto.getRoom().getDetail());
        reserv.setRoomCheckInTime(reservDto.getRoom().getCheckInTime());
        reserv.setRoomCheckOutTime(reservDto.getRoom().getCheckOutTime());
        reserv.setRoomPrice(reservDto.getRoom().getPrice());
        reserv.setReservName(reservDto.getReservName());
        reserv.setReservPN(reservDto.getReservPN());

        reserv.setReservationStatus(ReservationStatus.RESERVED);
        reserv.setReservDate(LocalDateTime.now());
        return reserv;
    }

    // 예약 취소
    public void cancelReserv() {
        this.reservationStatus = ReservationStatus.AVAILABLE;
    }

}
