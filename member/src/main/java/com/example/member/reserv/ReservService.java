package com.example.member.reserv;

import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.LodgingRepository;
import com.example.member.repository.MemberRepository;
import com.example.member.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservService {
    private final MemberRepository memberRepository;
    private final ReservRepository reservRepository;
    private final RoomRepository roomRepository;
    private final LodgingRepository lodgingRepository;

    public void saveReserv(ReservDto reservDto){
        Room room = reservDto.getRoom();
        Lodging lodging = room.getLodging();
        Reserv reserv = Reserv.createReserv(reservDto, lodging);
        validateDuplicateMember(reserv);
        reservRepository.save(reserv);
    }

    // 예약하려는 방의 상태를 가져와 예약 가능인지 검증
    private void validateDuplicateMember(Reserv reserv){
        Room reservRoom = roomRepository.findById(reserv.getRoom().getId())
                .orElseThrow(EntityNotFoundException::new);

        if(reservRoom.getReservationStatus().equals("RESERV")){
            throw new IllegalStateException("이미 예약된 숙소입니다");
        }
    }

    public ReservDto newReserv(Long roomId, Principal principal) throws Exception{
        ReservDto reservDto = new ReservDto();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);
        reservDto.setReservPN(ReservDto.phoneNumber(member));
        reservDto.setReservName(member.getName());
        reservDto.setRoom(room); // room id
        reservDto.setMember(member); // member email
        return reservDto;
    }


    // 예약 리스트 만들기
    public List<ReservDto> reservDtoList(Reserv reserv, Principal principal){
//        List<Reserv> member = reservRepository.findReservs(principal.getName());
        // principal 사용해서 래파지토리에서 findReservs 쓰기
//        Member member = reserv.getMember();
//        Long member_id = member.getId();
//        List<Reserv> reservList = reservRepository.findAllByMemberId(member_id);
//        List<ReservDto> reservDtoList = new ArrayList<>();

        String email = principal.getName();
        List<Reserv> reservList = reservRepository.findReservs(email);
        List<ReservDto> reservDtoList = new ArrayList<>();

        for(Reserv savedReserv : reservList){
            ReservDto reservDto = ReservDto.toReservDto(savedReserv);
            reservDtoList.add(reservDto);
        }
        System.out.println("reservDtoLsit : "+ reservDtoList);
        return reservDtoList;
    }
//    public List<ReservDto> reservDtoList(){
//        List<Reserv> reservList = reservRepository.findAll();
//        List<ReservDto> reservDtoList = new ArrayList<>();
//
//        for(Reserv reserv : reservList){
//            ReservDto reservDto = ReservDto.toReservDto(reserv);
//            reservDtoList.add(reservDto);
//        }
//        return reservDtoList;
//    }
//
//
//
//
//    // Controller로 부터 ReservId를 넘겨받아
//    // 예약한 숙소의 상태를 변경 시키는 Reserv의 cancelReserv() 메서드 호출
//    public void cancelReserv(Long reservId) {
//        Reserv reserv = reservRepository.findById(reservId)
//                .orElseThrow(EntityNotFoundException::new);
//        reserv.cancelReserv();
//    }
//
//
//
//        // 숙소명, 방이름, 방디테일, 체크인아웃, 방가격,   예약자의 이름,전화전호
//
//
//    // 주문 내역 조회 시 필요한 데이터
//    // reservID,숙소네임,숙소 대표이미지, roomId,룸 네임 , memberId, reservName,
//    // reservPN,방가격, 예약날짜, 상태, 체크인아웃, 인원(?),
//
//    // room
//    // 숙소네임,숙소 대표이미지, roomId,룸 네임,방가격,상태, 체크인아웃, 인원(?)
//
//    // reserv
//    // reservId, reservName, reservPN, 예약날짜
//
//    // member
//    // memberId
//
//
////    public boolean validateCancelReserv(Long reservId, String email) {
////        Member member = memberRepository.findByEmail(email).orElse(null);
////        Reserv reserv = reservRepository.findById(reservId).orElse(null);
////
////        if(StringUtils.equals(member, reserv.getMember().getEmail())){
////            return true;
////        }
////        return false;
////    }
}
