package com.example.member.controller;

import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.ItemImg;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.ItemImgRepository;
import com.example.member.repository.LodgingRepository;
import com.example.member.reserv.Reserv;
import com.example.member.reserv.ReservDto;
import com.example.member.reserv.ReservService;
import com.example.member.reserv.reservDate.ReservedDateDto;
import com.example.member.reserv.reservDate.ReservedDateService;
import com.example.member.service.ItemImgService;
import com.example.member.service.LodgingService;
import com.example.member.service.RoomService;
import com.example.member.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservSellerController {

    private final LodgingService lodgingService;
    private final LodgingRepository lodgingRepository;
    private final RoomService roomService;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    private final UploadFileService uploadFileService;
    private final ReservedDateService reservedDateService;

    @GetMapping(value = "/reserv/lodgingReservList")
    public String toRservLodgingList(Model model) {

        List<LodgingDto> lodgingDtoList = lodgingService.lodgingDtos();
        uploadFileService.emptyUploadFileCheck();
        uploadFileService.backwardUploadFileCheck();

        for (int i = 0; i < lodgingDtoList.size(); i++) {
            // 숙소 DTO i번쨰 꺼내오기
            LodgingDto lodgingDto = lodgingDtoList.get(i);
            // 꺼내온 숙소 DTO의 아이디를 조회하고 아이디에 맞는 이미지들을 리스트로 뽑아오기
            List<ItemImg> itemImgList = itemImgRepository.findByLodgingId(lodgingDto.getId());

            List<ItemImgDto> itemImgDtoList = new ArrayList<>();

            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.toItemImgDto(itemImg);
                itemImgDtoList.add(itemImgDto);
            }

            // 숙소 DTO 대표 imgUrl을 저장하기 위한 과정
            for (int l = 0; l < itemImgDtoList.size(); l++) {
                ItemImgDto itemImgDto = itemImgDtoList.get(l);

                if (itemImgDto.getRepimgYn().equals("Y") && itemImgDto.getLodging().getId().equals(lodgingDto.getId())) {
                    lodgingDto.setImgUrl(itemImgDto.getImgUrl());
                }
            }

            // 숙소 DTO에 이미지 DTO 저장
            lodgingDto.setItemImgDtoList(itemImgDtoList);
            // 다시 숙소 DTO에 저장
            lodgingDtoList.set(i, lodgingDto);


        }

        model.addAttribute("lodgingDtoList", lodgingDtoList);
//        model.addAttribute("itemImgDtoList", itemImgDtoList);

        return "reserv/lodgingReservList";
    }


    @GetMapping(value = "/reserv/lodgingReservContent/{lodging_id}")
    public String toReservLodgingContent(@PathVariable("lodging_id") Long lodgingId, Model model) throws Exception {

        try{
            Lodging lodgingEntity = lodgingService.findById(lodgingId);
            LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodgingEntity);
            LodgingDto lodgingDtoContainImage =  lodgingService.imageLoad(lodgingDto, lodgingId);

            lodgingService.emptyRoomGrantedLodgingId(lodgingId, lodgingEntity);
            uploadFileService.refreshUploadFileCheck(lodgingId);

            // 숙소의 id값을 가지고 있는 방을 List로 호출한다.
            List<RoomDto> roomDtoList = roomService.roomDtoList(lodgingId);

            // 호출된 List에서 오늘, 내일 예약이 잡혀있는(예약이 불가한)
            // 방들은 제외한 후 보여준다.
            List<RoomDto> resultRoomDtoList =reservedDateService.defaultValidation(roomDtoList);

            System.out.println("이 이상 도달하지 못함");

            List<RoomDto> roomDtoListContainImage = roomService.imageLoad(resultRoomDtoList);

            model.addAttribute("lodgingDto", lodgingDtoContainImage);
            model.addAttribute("roomDtoList", roomDtoListContainImage);
            model.addAttribute("prevPage", "LodgingController");
        }catch (Exception e){
            model.addAttribute("lodgingErrorMsg", e.getMessage());
        }


        return "reserv/lodgingReservContent";
    }
        @PostMapping(value = "/reserv/lodgingReservContent/{lodging_id}")

    public String newCheckDate(Reserv reserv, Room room,
                               @RequestParam("checkIn") String checkIn,
                               @RequestParam("checkOut") String checkOut){ // 룸 디티오 말고

            System.out.println("paramCheckIn = " +checkIn);
            System.out.println("paramCheckOut = " +checkOut);

            reserv.setCheckIn(checkIn);
            reserv.setCheckOut(checkOut);
            return "/reserv/lodgingReservContent/{lodging_id}";

    }

}
