package com.example.member.reserv.reservDate;

import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.reserv.Reserv;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "rev_date_table")
public class ReservedDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column
    private LocalDate reserved_date;
}
