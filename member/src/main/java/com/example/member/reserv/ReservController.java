package com.example.member.reserv;

import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReservController {
    private final ReservService reservService;

    private final MemberRepository memberRepository;

    // 예약하기 버튼을 눌렀을 때
    @GetMapping("/{id}/reserv") // roomId/reserv
    public String newReserv (@PathVariable Long roomId , Principal principal, Model model){
        // 숙소명 찾기
        Lodging lodging = reservService.reservLodging(roomId);
        String lodgingName = lodging.getName();
        // 방 이름, 디테일, 체크인아웃티임, 가격 찾기
        Room room = reservService.reservRoom(roomId);
        String roomName = room.getName();
        String roomDetail = room.getDetail();
        String roomCheckInTime = room.getCheckInTime();
        String roomCheckOutTime = room.getCheckOutTime();
        String roomPrice = room.getPrice();
        // 예약자의 이름, 전화번호 찾기
        Member member = reservService.reservMember(principal);
        String reservName = member.getName();
        String reservPN = member.getPhoneN1()+"-"+member.getPhoneN2()+"-"+member.getPhoneN3();

        model.addAttribute("lodgingName", lodgingName);
        model.addAttribute("roomName", roomName);
        model.addAttribute("roomDetail",roomDetail);
        model.addAttribute("roomCheckInTime",roomCheckInTime);
        model.addAttribute("roomCheckOutTime",roomCheckOutTime);
        model.addAttribute("roomPrice",roomPrice);
        model.addAttribute("reservName",reservName);
        model.addAttribute("reservPN",reservPN);
        return "reserv/reserv";
    }

    @PostMapping("/{id}/reserv")
    public String saveReserv(@PathVariable Long roomId,@Valid ReservDto reservDto){
        Reserv reserv = Reserv.createReserv(reservDto);
        reservService.saveReserv(reserv);

        return "redirect:/reserv/reservHist";
    }


    // 예약 내역
    @GetMapping({"/reservs","/reservs/{page}"})
    public String reservHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,5);
        Page<ReservHistDto> reservHistDtoList = reservService.getReservList(principal.getName(), pageable);
        // principal.getName() 현재 로그인된  사용자의 이메일

        model.addAttribute("reservs", reservHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "reserv/reservHist";
    }

    // 예약 취소
    @PostMapping("/reserv/{reservId}/cancel")
    public @ResponseBody ResponseEntity cancelReserv(
            @PathVariable("reservId") Long reservId, Principal principal){
        // 주문 취소 시 reservId와 접속중인 사용자의 이메일을 가져와서
        // 접속중인 USER가 예약한 USER가 맞는지 비교하여 return
        if(!reservService.validateReserv(reservId,principal.getName())){
            return new ResponseEntity<String>("예약 취소 권한이 없습니다",HttpStatus.FORBIDDEN);
        }
        // 예약 취소한 USER가 예약 요청한 USER가 맞을시 ReservService의 cancelReserv() 메서드 호출
        reservService.cancelReserv(reservId);
        return new ResponseEntity<Long>(reservId, HttpStatus.OK);
    }
}
