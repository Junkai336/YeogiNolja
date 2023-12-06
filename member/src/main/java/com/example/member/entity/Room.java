package com.example.member.entity;

import com.example.member.constant.ReservationStatus;
import com.example.member.dto.RoomDto;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="room")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_id")
    private Long id;

    @JoinColumn(name = "lodging_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lodging lodging;

//    @JoinColumn(name = "member_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member member;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column
    private String name;

    @Column
    private String detail;

    @Column
    private String adult;

    @Column
    private String children;

    @Column
    private String price;

    @Column
    private String checkInTime;

    @Column
    private String checkOutTime;

//    @JoinColumn
//    private List<ItemImg> itemImgList = new ArrayList<>();


    public static Room toRoom(RoomDto roomDto, Lodging lodging) {
        Room room = new Room();
        room.setId(roomDto.getId());
        room.setLodging(lodging);
        room.setReservationStatus(roomDto.getReservationStatus());
        room.setName(roomDto.getName());
        room.setDetail(roomDto.getDetail());
        room.setAdult(roomDto.getAdult());
        room.setChildren(roomDto.getChildren());
        room.setPrice(roomDto.getPrice());
        room.setCheckInTime(roomDto.getCheckInTime());
        room.setCheckOutTime(roomDto.getCheckOutTime());

        return room;
    }
    // Entity 병합용

    //    2023-12-09T10:53
//    public static String timeFormat(String checkIn) {
//        String[] checkTime = checkIn.split("T");
//        System.out.println("checkTime[0]:" + checkTime[0]);
//        System.out.println("checkTime[1]:" + checkTime[1]);
//        String[] strsplit = checkTime[0].split("-");
//        System.out.println(strsplit);
//        int strsplit11 = Integer.parseInt(strsplit[0]);
//        int strsplit22 = Integer.parseInt(strsplit[1]);
//        int strsplit33 = Integer.parseInt(strsplit[2]);
//        LocalDate localDate = LocalDate.of(strsplit11, strsplit22, strsplit33);
//        System.out.println(localDate.toString());
//        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
//
//        int dayOfWeekNumber = dayOfWeek.getValue();
//
//        System.out.println(dayOfWeekNumber);
//        String day = "";
//        switch (dayOfWeekNumber) {
//            case 1:
//                day = "월";
//                break;
//            case 2:
//                day = "화";
//                break;
//            case 3:
//                day = "수";
//                break;
//            case 4:
//                day = "목";
//                break;
//            case 5:
//                day = "금";
//                break;
//            case 6:
//                day = "토";
//                break;
//            case 7:
//                day = "일";
//                break;
//        }
//        String totalDate = checkTime[0] + "(" + day + ")" + " " + checkTime[1];
//        System.out.println("totalDate = " + totalDate);
//        return totalDate;
//    }
}
