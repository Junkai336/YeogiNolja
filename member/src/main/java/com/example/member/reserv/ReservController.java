package com.example.member.reserv;


import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.Lodging;
import com.example.member.repository.MemberRepository;
import com.example.member.reserv.reservDate.ReservedDateDto;
import com.example.member.reserv.reservDate.ReservedDateService;
import com.example.member.service.LodgingService;
import com.example.member.service.RoomService;
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
@RequestMapping("/reserv")
public class ReservController {
    private final ReservService reservService;
    private final ReservedDateService reservedDateService;
    private final LodgingService lodgingService;
    private final RoomService roomService;


    // 예약하기 버튼을 눌렀을 때 예약 결제창
    @GetMapping("/roomReservation/{room_id}/{sessionDate}") // roomId/reserv
    public String newReserv (@PathVariable("room_id") Long roomId,
                             @PathVariable("sessionDate") String date,
                             Principal principal,
                             Reserv reserv, Model model){

//        System.out.println("roomId = "+roomId);
//        System.out.println("Date = "+date);

        try {
            ReservDto reservDto = reservService.newReserv(roomId, principal,reserv,date);
            model.addAttribute("reservDto", reservDto);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "reserv/reservPage";
    }

    @PostMapping("/roomReservation/{room_id}")
    public String saveReserv(@Valid ReservDto reservDto, BindingResult result,Model model
     ,Principal principal){
        String email = principal.getName();
        if(result.hasErrors()){
           return "reserv/reservPage";
        }
//        System.out.println("reserDto = "+ reservDto);
//        System.out.println("reservDto.getCheckIn() = "+reservDto.getCheckIn());
//        System.out.println("reservDto.getCheckOut() = "+reservDto.getCheckOut());
        try {
//           List<ReservDto> reservDtoList =
                   reservService.saveReserv(reservDto);
//           model.addAttribute("reservDtoList",reservDtoList);

        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "reserv/reservPage";
        }

        return "redirect:/reserv/reservs";
    }


    // 예약 내역
    @GetMapping({"/reservs","/reservs/{page}"})
    public String reservHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,4);
        List<ReservDto> reservDtoList = reservService.reservDtoList(principal);
        // principal.getName() 현재 로그인된  사용자의 이메일


        model.addAttribute("reservDtoList", reservDtoList);

        return "reserv/reservHist";
    }

    // 예약 취소
    @PostMapping("/{reservId}/cancel")
    public @ResponseBody ResponseEntity cancelReserv(
            @PathVariable("reservId") Long reservId, Principal principal){
        // 주문 취소 시 reservId와 접속중인 사용자의 이메일을 가져와서
        // 접속중인 USER가 예약한 USER가 맞는지 비교하여 return
        String email = principal.getName();
        if(reservService.validateCancelReserv(reservId,email)) {
            return new ResponseEntity<String>("예약 취소 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        // 예약 취소한 USER가 예약 요청한 USER가 맞을시 ReservService의 cancelReserv() 메서드 호출
        reservService.cancelReserv(reservId);
        return new ResponseEntity<Long>(reservId, HttpStatus.OK);
    }


    @GetMapping("/{lodgingId}/checkIn={checkIn}/checkOut={checkOut}")
    public String dateForm(@PathVariable("lodgingId") Long lodging_id,
                         @PathVariable("checkIn") String checkIn,
    @PathVariable("checkOut") String checkOut, Model model){

        try{
            Lodging lodgingEntity = lodgingService.findById(lodging_id);
            LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodgingEntity);
            LodgingDto lodgingDtoContainImage =  lodgingService.imageLoad(lodgingDto, lodging_id);

            for(ItemImgDto itemImgDto : lodgingDtoContainImage.getItemImgDtoList()) {
                System.out.println(itemImgDto);
            }

            lodgingService.emptyRoomGrantedLodgingId(lodging_id, lodgingEntity);
            // 숙소의 id값을 가지고 있는 방을 List로 호출한다.
            List<RoomDto> roomDtoList = roomService.roomDtoList(lodging_id);
            // 호출된 List에서 오늘, 내일 예약이 잡혀있는(예약이 불가한)
            // 방들은 제외한 후 보여준다.
            List<RoomDto> resultRoomDtoList =reservedDateService.defaultValidation(roomDtoList, checkIn, checkOut);

            List<RoomDto> roomDtoListContainImage = roomService.imageLoad(resultRoomDtoList);

            model.addAttribute("lodgingDto", lodgingDtoContainImage);
            model.addAttribute("roomDtoList", roomDtoListContainImage);
            model.addAttribute("prevPage", "LodgingController");
            model.addAttribute("checkIn", checkIn);
            model.addAttribute("checkOut", checkOut);
        }catch (Exception e){
            model.addAttribute("lodgingErrorMsg", e.getMessage());
        }

        System.out.println("checkIn = "+checkIn+ "//"+"checkOut = "+ checkOut);

        return "reserv/lodgingReservContent";
    }
}
