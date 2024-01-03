package com.example.member.entity;

import com.example.member.constant.LodgingType;
import com.example.member.constant.RoomExist;
import com.example.member.dto.LodgingDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="lodging")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Lodging extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="lodging_id")
    private Long id;


    @OneToMany(mappedBy = "lodging", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Room> room = new ArrayList<>();

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @Column
    private String name;

    @Column
    private String detail;

    @Column
    private String postcode;   // 우편 번호

    @Column
    private String address;     // 주소

    @Column
    private String detailAddress; // 상세주소

    @Column
    private String extraAddress; // 참고항목

    @Enumerated(EnumType.STRING)
    private LodgingType lodgingType;

    @Enumerated(EnumType.STRING)
    private RoomExist roomExist;

    // 매개변수 Room 추가 고려
    public static Lodging toLodging(Member member, LodgingDto lodgingDto) {
        Lodging lodging = new Lodging();

        lodging.setId(lodgingDto.getId());
        lodging.setRoom(lodgingDto.getRoomList());
        lodging.setMember(member);
        lodging.setRoomExist(lodgingDto.getRoomExist());
        lodging.setName(lodgingDto.getName());
        lodging.setDetail(lodgingDto.getDetail());
        lodging.setPostcode(lodgingDto.getPostcode());
        lodging.setAddress(lodgingDto.getAddress());
        lodging.setDetailAddress(lodgingDto.getDetailAddress());
        lodging.setExtraAddress(lodgingDto.getExtraAddress());
        lodging.setLodgingType(lodgingDto.getLodgingType());
        lodging.setRegTime(lodgingDto.getRegTime());
        lodging.setUpdateTime(lodgingDto.getUpdateTime());
        return lodging;
    }

}
