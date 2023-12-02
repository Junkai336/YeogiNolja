package com.example.member.reserv;

import com.example.member.entity.ItemImg;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.LodgingImgRepository;
import com.example.member.repository.LodgingRepository;
import com.example.member.repository.MemberRepository;
import com.example.member.repository.RoomRepository;
import com.example.member.reservItem.ReservItem;
import com.example.member.reservItem.ReservItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservService {
    private final LodgingRepository lodgingRepository;
    private final MemberRepository memberRepository;
    private final ReservRepository reservRepository;
    private final LodgingImgRepository lodgingImgRepository;
    private final RoomRepository roomRepository;


    public Reserv saveReserv(Reserv reserv) {

        return reservRepository.save(reserv);
    }
//    public Long reserv(ReservDto reservDto, String email) {
//        Room room = roomRepository.findById(reservDto.getRoomId())
//                .orElseThrow(EntityNotFoundException::new);
//        Member member = memberRepository.findByEmail(email).orElse(null);
//        // 현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 조회
//
//        Reserv reserv = Reserv.createReserv(member, room);
//        // 회원정보와 예약할 방 정보를 이용하여 예약 엔티티 생성
//        reservRepository.save(reserv);
//        return reserv.getId();
//    }

    @Transactional(readOnly = true)
    // OrderService 클래스 자체에 @Transactional이 적용되어 있으므로
    // 데이터를 읽기만 하는 메서드에서는 @Transactional 이 적용되어 데이터를 읽어들이는 시간을 없애기위해
    // @Transactional(readOnly = true) 을 적용하여 @Transactional을  제외시킨다.
    public Page<ReservHistDto> getReservList(String email, Pageable pageable){
        List<Reserv> reservs = reservRepository.findReservs(email,pageable);
        // 유저 아이디와 페이징 조건을 이용해서 주문 목록 조회
        Long totalCount = reservRepository.countReserv(email);
        // 유저의 주문 총 개수를 구함
        List<ReservHistDto> reservHistDtos = new ArrayList<>();
        for(Reserv reserv : reservs){
            // 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 Dto를 생성
            ReservHistDto reservHistDto = new ReservHistDto(reserv);
            List<ReservItem> reservItems = reserv.getReservItems();
            for(ReservItem reservItem : reservItems){
                ItemImg itemImg = lodgingImgRepository.findBylodgingIdAndRepimgYn(reservItem.getLodging().getId(),"Y");
                // 주문 상품의 대표 이미지를 조회
                // 주문 상품이 대표 이미지를 조회하여 parameter로 넘기는 ItemId와 repImgYn 를 충족하는 itemImg를 가져온다.
                ReservItemDto reservItemDto = new ReservItemDto(reservItem,itemImg.getImgUrl());
                reservHistDto.addReservItemDto(reservItemDto);
            }
            reservHistDtos.add(reservHistDto);
        }
        return new PageImpl<ReservHistDto>(reservHistDtos,pageable,totalCount);
        // 페이지 구현객체를 생성하여 반환함

        // 정리!
        // 주문이력에 출력할 주문 일자, 주문상태, 주문 상품들을 가지는 orderHistDTO를 생성하여
        //사용자의 주문목록을 확인하여 총 주문갯수를 totalCount에 저장한다.
        // 사용자의 주문 목록에서 하나의 주문 마다 가지고 있는 주문 상품을 꺼내서
        // 주문 상품의 id값과 repimgYn 값이 맞는 (id값에 맞는 상품의 대표이미지)를 저장
        // 주문 상품과 대표이미지의 URL을 OrderItemDTo를 생성
        // => 저장된 OrderItemDTo에서 필요한 값들을 꺼내어 출력할 주문 일자, 주문상태, 주문 상품들을 가지는 orderHistDTO 생성
        // 생성된 orderHistDto들을 리스트로 만들어서 PageImpl(페이지 구현객체 : [springframework.data.domain]) 에
        // orderHistDTO 리스트와 pageable, 총 주문 수량을 리턴한다.

    }

    @Transactional(readOnly = true)
    public boolean validateReserv(Long reservId, String email) {
        Member curMember = memberRepository.findByEmail(email).orElse(null);
        Reserv reserv = reservRepository.findById(reservId)
                .orElseThrow(EntityNotFoundException::new);
        Member saveMember = reserv.getMember();

        if(!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())){
            return false;
        }
        return true;
    }

    // Controller로 부터 ReservId를 넘겨받아
    // 예약한 숙소의 상태를 변경 시키는 Reserv의 cancelReserv() 메서드 호출
    public void cancelReserv(Long reservId) {
        Reserv reserv = reservRepository.findById(reservId)
                .orElseThrow(EntityNotFoundException::new);
        reserv.cancelReserv();
    }


    public String findName(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        String memberName = member.getName();
        return memberName;

    }

        // 숙소명, 방이름, 방디테일, 체크인아웃, 방가격,   예약자의 이름,전화전호
    public Lodging reservLodging(Long roomId) {
        // 숙소명 찾기
        Lodging lodging = (Lodging) roomRepository.findAllByLodgingId(roomId);
        return lodging;
    }
    public Room reservRoom(Long roomId) {
        // 방 이름, 디테일, 체크인아웃티임, 가격 찾기
        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);
        return room;
    }
    public Member reservMember(Principal principal) {
        // 예약자의 이름, 전화번호 찾기
        // String.valueOf() 해당 타입 파라미터의 값을 문자열로 변환해준다 NullPointerException 방지
        Member member = memberRepository.findByEmail(String.valueOf(principal))
                .orElseThrow(EntityNotFoundException::new);
        return member;
    }


}
