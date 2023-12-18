package com.example.member.reserv;

import com.example.member.article.Article;
import com.example.member.article.ArticleDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.LodgingRepository;
import com.example.member.repository.MemberRepository;
import com.example.member.repository.RoomRepository;
import com.example.member.reserv.reservDate.ReservedDate;
import com.example.member.reserv.reservDate.ReservedDateDto;
import com.example.member.reserv.reservDate.ReservedDateRepository;
import com.example.member.reserv.reservDate.ReservedDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final ReservedDateService reservedDateService;
    private final ReservedDateRepository reservedDateRepository;

    public void saveReserv(ReservDto reservDto, List<LocalDate> dateList)throws Exception{
        Room room = reservDto.getRoom();
        Lodging lodging = room.getLodging();
        Reserv reserv = Reserv.createReserv(reservDto, lodging);
        validateDuplicateMember(reserv);
        reservRepository.save(reserv);

        reservedDateService.saveReservDate(room, dateList);


    }

    // 예약하려는 방의 상태를 가져와 예약 가능인지 검증
    private void validateDuplicateMember(Reserv reserv){
        Room reservRoom = roomRepository.findById(reserv.getRoom().getId())
                .orElseThrow(EntityNotFoundException::new);

        if(reservRoom.getReservationStatus().equals("RESERV")){
            throw new RuntimeException("이미 예약된 숙소입니다");
        }
    }

    public ReservDto newReserv(Long roomId, Principal principal,String date) throws Exception{
        ReservDto reservDto = new ReservDto();
        String[] checkDate = date.split("~");

        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);
        Lodging lodging = lodgingRepository.findById(room.getLodging().getId())
                .orElseThrow(EntityNotFoundException::new);
        reservDto.setLodging(lodging);
        reservDto.setReservPN(ReservDto.phoneNumber(member));
        reservDto.setReservName(member.getName());
        reservDto.setRoom(room); // room id
        reservDto.setMember(member); // member email
        reservDto.setCheckIn(checkDate[0]);
        reservDto.setCheckOut(checkDate[1]);

        List<LocalDate> checkDateList = reservedDateService.toLocalDate(checkDate[0], checkDate[1]);
        int countDays = checkDateList.size()-1;
        int roomPrice = Integer.parseInt(room.getPrice());
        reservDto.setTotalPrice(countDays*roomPrice);
        return reservDto;
    }

    // 예약 리스트 만들기
    public List<ReservDto> reservDtoList(Principal principal){
        String email = principal.getName();
        List<Reserv> reservList = reservRepository.findReservs(email);
        List<ReservDto> reservDtoList = new ArrayList<>();

        for(Reserv savedReserv : reservList){
            ReservDto reservDto = ReservDto.toReservDto(savedReserv);
//            System.out.println(reservDto.getReservationStatus());
            reservDtoList.add(reservDto);
        }
        return reservDtoList;
    }

    // Controller로 부터 ReservId를 넘겨받아
    // 예약한 숙소의 상태를 변경 시키는 Reserv의 cancelReserv() 메서드 호출
    public void cancelReserv(Long reservId) {
        Reserv reserv = reservRepository.findById(reservId)
                .orElseThrow(EntityNotFoundException::new);
        reservedDateService.cancleDate(reserv);
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
    @Transactional(readOnly = true)
    public Page<ReservDto> getReservList(String email, Pageable pageable) {
        List<Reserv> reservList = reservRepository.findReservsPaging(email, pageable);
        Long totalCount = reservRepository.countReserv(email);

        List<ReservDto> reservDtoList = new ArrayList<>();

        for(Reserv reserv : reservList) {
            ReservDto reservDto = ReservDto.toReservDto(reserv);
            reservDtoList.add(reservDto);
        }

        return new PageImpl<ReservDto>(reservDtoList, pageable, totalCount);
    }

    public List<Reserv> findAll() {
        List<Reserv> reservList = reservRepository.findAll();
        return reservList;
    }

    public boolean validateCheckDate(Reserv reserv, Long roomId, String checkIn, String checkOut) throws Exception {

        List<LocalDate> reservDateList = reservedDateService.toLocalDate(checkIn, checkOut);
        List<ReservedDate> reservedDateList = reservedDateRepository.findAll();

        for(ReservedDate reservedDate : reservedDateList) {
            for(LocalDate reservDate : reservDateList) {
                if(reservedDate.getReserved_date().equals(reservDate)) {
                    System.out.println(reservDate);
                    System.out.println("duplication !!!!!!!!!!!!!!!!!!!");
                    return true;
                }
            }
        }

        System.out.println("no duplication");

        return false;
    }
}
