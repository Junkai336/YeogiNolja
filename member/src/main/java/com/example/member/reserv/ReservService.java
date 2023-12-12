package com.example.member.reserv;

import com.example.member.dto.RoomDto;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.LodgingRepository;
import com.example.member.repository.MemberRepository;
import com.example.member.repository.RoomRepository;
import com.example.member.reserv.reservDate.ReservedDateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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

        public static void newCheckDateTime(ReservDto checkForm, Room room) {
        String newCheckDateTime1 = checkForm.getCheckIn()+room.getCheckInTime();
        String newCheckDateTime2 = checkForm.getCheckOut()+room.getCheckOutTime();
        checkForm.setCheckIn(newCheckDateTime1);
        checkForm.setCheckOut(newCheckDateTime2);
        System.out.println("checkFormEx = "+checkForm);
    }

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

    public ReservDto newReserv(Long roomId, Principal principal,Reserv reserv) throws Exception{
        ReservDto reservDto = new ReservDto();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);
        reservDto.setReservPN(ReservDto.phoneNumber(member));
        reservDto.setReservName(member.getName());
        reservDto.setRoom(room); // room id
        reservDto.setMember(member); // member email
        reservDto.setCheckIn(reserv.getCheckIn());
        return reservDto;
    }

    // 예약 리스트 만들기
    public List<ReservDto> reservDtoList(Principal principal){
        String email = principal.getName();
        List<Reserv> reservList = reservRepository.findReservs(email);
        List<ReservDto> reservDtoList = new ArrayList<>();

        for(Reserv savedReserv : reservList){
            ReservDto reservDto = ReservDto.toReservDto(savedReserv);
            System.out.println(reservDto.getReservationStatus());
            reservDtoList.add(reservDto);
        }
        return reservDtoList;
    }

    // Controller로 부터 ReservId를 넘겨받아
    // 예약한 숙소의 상태를 변경 시키는 Reserv의 cancelReserv() 메서드 호출
    public void cancelReserv(Long reservId) {
        Reserv reserv = reservRepository.findById(reservId)
                .orElseThrow(EntityNotFoundException::new);
        reserv.cancelReserv();
    }

    // 예약자, 접속자가 동일인물인지 검증
    public boolean validateCancelReserv(Long reservId, String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        Reserv reserv = reservRepository.findById(reservId).orElse(null);

        if(StringUtils.equals(member, reserv.getMember().getEmail())){
            return true;
        }
        return false;
    }
}
