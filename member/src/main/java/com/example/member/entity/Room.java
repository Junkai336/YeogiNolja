package com.example.member.entity;

import com.example.member.constant.ReservationStatus;
import lombok.*;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column
    private String name;

    @Column
    private String detail;

    @Column
    private String price;

    @Column
    private String checkInTime;

    @Column
    private String checkOutTime;




}
