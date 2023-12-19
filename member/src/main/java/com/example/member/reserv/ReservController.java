package com.example.member.reserv;


import com.example.member.constant.UserRole;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.MemberDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.reserv.reservDate.ReservedDateService;
import com.example.member.service.LodgingService;
import com.example.member.service.MemberService;
import com.example.member.service.RoomService;
import com.example.member.service.UploadFileService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.time.LocalDate;
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
    private final UploadFileService uploadFileService;
    private final MemberService memberService;

    // 예약하기 버튼을 눌렀을 때 예약 결제창
    @GetMapping("/roomReservation/{lodgingId}/{room_id}/{sessionDate}") // roomId/reserv
    public String newReserv (@PathVariable("room_id") Long roomId,
                             @PathVariable("lodgingId") Long lodgingId,
                             @PathVariable("sessionDate") String date,
                             Principal principal, Model model){


        try {
            ReservDto reservDto = reservService.newReserv(roomId, principal,date);
            reservDto.setLodging(lodgingService.findById(lodgingId));

            model.addAttribute("reservDto", reservDto);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "reserv/reservPage";
    }


    // 예약 내역
    @GetMapping({"/reservs","/reservs/{page}"})
    public String reservHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<ReservDto> reservDtoList = reservService.getReservList(principal.getName(), pageable);

        model.addAttribute("reservDtoList", reservDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

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
                           @PathVariable("checkOut") String checkOut, Model model,
                           Principal principal){
        System.out.println("예약 일자 등록 시 숙소 id 값 : "+lodging_id);
        String email = principal.getName();
        MemberDto memberDto = memberService.DtofindByEmail(email);

        try{
            Lodging lodgingEntity = lodgingService.findById(lodging_id);
            LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodgingEntity);
            LodgingDto lodgingDtoContainImage =  lodgingService.imageLoad(lodgingDto, lodging_id);

            lodgingService.emptyRoomGrantedLodgingId(lodging_id, lodgingEntity);
            if (memberDto.getUserRole().equals(UserRole.ADMIN)) {
                // 관리자 일떄 (모든 방들을 보여준다.)
                List<RoomDto> roomDtoList = roomService.roomDtoList(lodging_id);
                List<RoomDto> roomDtoListContainImage = roomService.imageLoad(roomDtoList);
                model.addAttribute("roomDtoList", roomDtoListContainImage);
            }else{
                // 일반 유저 일때 (예약이 가능한 방들을 보여줌)
                List<LocalDate> dateList = reservedDateService.toLocalDate(checkIn, checkOut);
                List<RoomDto> resultDtoList = reservedDateService.defaultValidation(lodging_id, dateList);
                System.out.println("resultDtoList :"+resultDtoList);
                // 호출된 List에서 오늘, 내일 예약이 잡혀있는(예약이 불가한)
                // 방들은 제외한 후 보여준다.
//                List<RoomDto> resultRoomDtoList =reservedDateService.defaultValidation(roomDtoList, checkIn, checkOut);
                List<RoomDto> roomDtoListContainImage = roomService.imageLoad(resultDtoList);
                model.addAttribute("roomDtoList", roomDtoListContainImage);
            }
            // 숙소의 id값을 가지고 있는 방을 List로 호출한다.
            model.addAttribute("lodgingDto", lodgingDtoContainImage);
            model.addAttribute("prevPage", "LodgingController");
        }catch (Exception e){
            model.addAttribute("lodgingErrorMsg", e.getMessage());
        }


        return "/reserv/lodgingReservContent";
    }

    // 결제 관련 수정중입니다.
    // 결제성공시 해당 메소드 진입 -> 결제 검증, httpStatus ok 리턴 -> window.location.href로 saveReserv를 리턴할 예정
    @PostMapping("reservationPay/{room_id}/{checkIn}/{checkOut}")
    @ResponseBody
    public HttpStatus reservationPay(
//                                        @RequestBody ReservSaveDto reservSaveDto
                                        @PathVariable("room_id") Long roomId,
                                        @PathVariable("checkIn") String checkIn,
                                        @PathVariable("checkOut") String checkOut
                                     )
                                     {
            // 예약 엔티티에서 결제할 Room의 Id와 동일한 Room Id가 있을 경우 예약일자 체크를 한다.
            // 체크인 날짜(checkIn) ~ 체크아웃 날짜(checkOut) 사이에 reserved Date가 있는지 체크를 하고,
            // 포함되는 날짜가 있다면 중복으로 판단한다. (roomId 중복 + reserved Date 중복)
//try {
//    System.out.println("hellos");
                                         System.out.println("helllllllllllll");
                                         System.out.println(roomId);
                                         System.out.println(checkIn);
                                         System.out.println(checkOut);
////
//        List<Reserv> reservList = reservService.findAll();
//
//        if(!reservList.isEmpty()) {
//            System.out.println("ok !reservList.isEmpty");
//        for(Reserv reserv : reservList) {
//            if(reserv.getRoom().getId().equals(reservSaveDto.getRoom_id())) {
//                System.out.println("enter boolean duplication");
//                boolean duplication = reservService.validateCheckDate(reserv, reservSaveDto.getRoom_id(), reservSaveDto.getCheckIn(), reservSaveDto.getCheckOut());
//                System.out.println("ok boolean duplication");
//                if(duplication) {
//                    System.out.println("error : 검증 실패: 객실 및 예약일자가 겹치므로 중복 예약");
//                    return HttpStatus.BAD_REQUEST;
//                }
//            }
//        }
//        }
//} catch (Exception e) {
//    System.out.println("error: try catch");
//    return HttpStatus.BAD_REQUEST;
//}

        System.out.println("success : 검증 성공");
        return HttpStatus.OK;
    }

    @PostMapping("/roomReservation/saveReserv")
    @ResponseBody
    public HttpStatus saveReserv(@RequestBody ReservSaveDto reservSaveDto,Model model
            ,Principal principal){

        Member member =  memberService.findById(reservSaveDto.getMember_id());
        Room room = roomService.findById(reservSaveDto.getRoom_id());
        Lodging lodging = lodgingService.findById(reservSaveDto.getLodging_id());

        ReservDto reservDto = ReservDto.toSaveReservDto(reservSaveDto, member, room, lodging);
        try {
        String email = principal.getName();
        Long lodgingId = lodging.getId();
        System.out.println("LodgingId : "+ lodgingId);
            List<LocalDate> reservDateList = reservedDateService.toLocalDate(reservDto.getCheckIn(), reservDto.getCheckOut());
            reservService.saveReserve(reservDto, reservDateList);
            System.out.println("예약저장");
        } catch (Exception e){
            System.out.println(e.getMessage());
//            return new ResponseEntity<ReservDto>(reservDto, HttpStatus.BAD_REQUEST);
            return HttpStatus.OK;
        }
//        return new ResponseEntity<ReservDto>(reservDto, HttpStatus.OK);
        return HttpStatus.BAD_REQUEST;
    }

}
