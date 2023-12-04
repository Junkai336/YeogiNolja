package com.example.member.controller;

import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.ItemImg;
import com.example.member.entity.Lodging;
import com.example.member.repository.ItemImgRepository;
import com.example.member.repository.LodgingRepository;
import com.example.member.repository.RoomRepository;
import com.example.member.service.ItemImgService;
import com.example.member.service.LodgingService;
import com.example.member.service.ReservSellerService;
import com.example.member.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservSellerController {

    private final LodgingService lodgingService;
    private final LodgingRepository lodgingRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    private final ReservSellerService reservSellerService;

    @GetMapping(value = "/reserv/lodgingReservList")
    public String toRservLodgingList(Model model) {

        List<LodgingDto> lodgingDtoList = lodgingService.lodgingDtos();

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

                if (itemImgDto.getRepImgYn().equals("Y") && itemImgDto.getLodging().getId().equals(lodgingDto.getId())) {
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
    public String toReservLodgingContent(@PathVariable("lodging_id") Long lodgingId, Model model) {

        Lodging lodging = lodgingRepository.findById(lodgingId).orElseThrow(EntityExistsException::new);

        LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodging);


        // ------------------------------------------------------------

        List<RoomDto> roomDtoList = roomService.roomDtoList(lodgingId);

        for (int i = 0; i < roomDtoList.size(); i++) {
            // 객실 DTO i번쨰 꺼내오기
            RoomDto roomDto = roomDtoList.get(i);
            // 꺼내온 숙소 DTO의 아이디를 조회하고 아이디에 맞는 이미지들을 리스트로 뽑아오기
            List<ItemImg> itemImgList = itemImgRepository.findByRoomId(roomDto.getId());

            List<ItemImgDto> itemImgDtoList = new ArrayList<>();

            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.toItemImgDto(itemImg);
                itemImgDtoList.add(itemImgDto);
            }

            // 숙소 DTO 대표 imgUrl을 저장하기 위한 과정
            for (int l = 0; l < itemImgDtoList.size(); l++) {
                ItemImgDto itemImgDto = itemImgDtoList.get(l);

                if (itemImgDto.getRepImgYn().equals("Y") && itemImgDto.getRoom().getId().equals(roomDto.getId())) {
                    roomDto.setImgUrl(itemImgDto.getImgUrl());
                }
            }

            // 숙소 DTO에 이미지 DTO 저장
            roomDto.setItemImgDtoList(itemImgDtoList);
            // 다시 숙소 DTO에 저장
            roomDtoList.set(i, roomDto);

            // -----------------------------------------------------------

            model.addAttribute("lodgingDto", lodgingDto);
            model.addAttribute("roomDtoList", roomDtoList);

        }

        return "reserv/lodgingReservContent";

    }
}
