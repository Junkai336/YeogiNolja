package com.example.member.reserv;


import com.example.member.article.ArticleDto;
import com.example.member.constant.UserRole;
import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.MemberDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.Lodging;
import com.example.member.repository.MemberRepository;
import com.example.member.reserv.reservDate.ReservedDateDto;
import com.example.member.reserv.reservDate.ReservedDateService;
import com.example.member.service.LodgingService;
import com.example.member.service.MemberService;
import com.example.member.service.RoomService;
import com.example.member.service.UploadFileService;
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
// 여기서부터 저장 시작****************************************************************
    @PostMapping("/roomReservation/saveReserv")
    public String saveReserv(@Valid ReservDto reservDto, BindingResult result,Model model
     ,Principal principal){
        String email = principal.getName();
        Long lodgingId = reservDto.getRoom().getLodging().getId();
        System.out.println("LodgingId : "+ lodgingId);
        if(result.hasErrors()){
           return "reserv/reservPage";
        }
        try {
            List<LocalDate> reservDateList = reservedDateService.toLocalDate(reservDto.getCheckIn(), reservDto.getCheckOut());
            reservService.saveReserv(reservDto, reservDateList);


        } catch (Exception e){
            System.out.println(e.getMessage());
            LodgingDto lodgingDto = lodgingService.findLodging(lodgingId);
            LodgingDto lodgingDtoContainImage =  lodgingService.imageLoad(lodgingDto, lodgingId);
            uploadFileService.refreshUploadFileCheck(lodgingId);
            lodgingService.emptyRoomGrantedLodgingId(lodgingId, lodgingService.findById(lodgingId));
            // 숙소의 id값을 가지고 있는 방을 List로 호출한다.
            List<RoomDto> roomDtoList = roomService.roomDtoList(lodgingId);
            // 호출된 List에서 오늘, 내일 예약이 잡혀있는(예약이 불가한)
            // 방들은 제외한 후 보여준다.
            List<RoomDto> resultRoomDtoList =reservedDateService.defaultValidation(roomDtoList);
            List<RoomDto> roomDtoListContainImage = roomService.imageLoad(resultRoomDtoList);

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("lodgingDto", lodgingDtoContainImage);
            model.addAttribute("roomDtoList", roomDtoListContainImage);
            model.addAttribute("prevPage", "LodgingController");
            return "reserv/lodgingReservContent";
        }

        return "redirect:/reserv/reservs";
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


    @GetMapping(value = {"/{lodgingId}/checkIn={checkIn}/checkOut={checkOut}", "/reserv/lodgingReservContent/{lodging_id}"})
    public String dateForm(@PathVariable("lodgingId") Long lodging_id,
                         @PathVariable("checkIn") String checkIn,
    @PathVariable("checkOut") String checkOut, Model model,
                           Principal principal){
        System.out.println("예약 일자 등록 시 숙소 id 값 : "+lodging_id);
        System.out.println("helloworld");
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
                List<RoomDto> roomDtoList = roomService.roomDtoList(lodging_id);

                // 호출된 List에서 오늘, 내일 예약이 잡혀있는(예약이 불가한)
                // 방들은 제외한 후 보여준다.
                List<RoomDto> resultRoomDtoList =reservedDateService.defaultValidation(roomDtoList, checkIn, checkOut);
                List<RoomDto> roomDtoListContainImage = roomService.imageLoad(resultRoomDtoList);
                model.addAttribute("roomDtoList", roomDtoListContainImage);
            }

            // 숙소의 id값을 가지고 있는 방을 List로 호출한다.




            model.addAttribute("lodgingDto", lodgingDtoContainImage);

            model.addAttribute("prevPage", "LodgingController");
        }catch (Exception e){
            model.addAttribute("lodgingErrorMsg", e.getMessage());
        }

        System.out.println("checkIn = "+checkIn+ "//"+"checkOut = "+ checkOut);

        return "reserv/lodgingReservContent";
    }

    // 결제 관련 수정중입니다.
    // 결제성공시 해당 메소드 진입 -> 결제 검증, httpStatus ok 리턴 -> window.location.href로 saveReserv를 리턴할 예정
    @PostMapping("reservationPay")
    @ResponseBody
    public HttpStatus reservationPay(int amount, Long roomId, String imp_uid, String merchant_uid) {
//        try {

        // price가 string으로 저장되기 때문에 int로 변환 과정을 거침
//        Room room = roomService.findById(roomId);
//        int roomPrice = Integer.parseInt(room.getPrice());
//
//        // 결제된 가격이 실제 객실 가격과 다르다면 badRequest를 보냄
//        if(roomPrice != amount) {
//            System.out.println("검증 실패 : 결제가격이 상품가격과 일치하지 않음");
//            return new ResponseEntity<String>("결제 가격이 실제 상품 가격과 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }

        // 해당 roomId를 가진 reserv Entity가 있을 경우 중복으로 판단 후 badRequest를 보냄
//        boolean duplication = reservService.validateHavingRoomId(room);
//        if(duplication) {
//            System.out.println("검증 실패: 중복 예약");
//            return new ResponseEntity<String>("이미 예약된 객실입니다.", HttpStatus.BAD_REQUEST);
//        }

        // 성공적인 경우
        System.out.println("검증 성공");
        return HttpStatus.OK;

//        } catch (Exception e) {
//            // try catch에 걸린 경우
//            System.out.println("검증 실패: try catch");
////            return new ResponseEntity<String>("결제에 문제가 발생했습니다.", HttpStatus.BAD_REQUEST);
//            return new ResponseEntity<String>("객실 예약에 성공했습니다.", HttpStatus.OK);
//        }

        // System.out.println("imp_uid : " + imp_uid);
        // System.out.println("merchant_uid : " + merchant_uid);

    }
}
