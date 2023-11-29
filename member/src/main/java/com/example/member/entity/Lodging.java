package com.example.member.entity;

import com.example.member.constant.LodgingType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="lodging")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Lodging extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lodging_id")
    private Long id;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
//    @OneToMany(fetch = FetchType.LAZY)
    private Room room;

//    @Column
//    private Review review;

    @Column
    private String name;

    @Column
    private String detail;

    @Column
    private String price;

    @Column
    private String location;

    @Enumerated(EnumType.STRING)
    private LodgingType lodgingType;





}
