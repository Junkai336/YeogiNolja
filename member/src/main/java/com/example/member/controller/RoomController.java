package com.example.member.controller;

import com.example.member.constant.ReservationStatus;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.LodgingRepository;
import com.example.member.repository.RoomRepository;
import com.example.member.service.LodgingService;
import com.example.member.service.RoomService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final LodgingService lodgingService;
    private final LodgingRepository lodgingRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;

    @GetMapping(value = "lodging/{id}/roomRegistration")
    public String fromLodgingDetailToRoomCreation(@PathVariable Long id, Model model, Principal principal) {
        String email = principal.getName();

        System.out.println(principal.getName());

        Lodging lodgingEntity = lodgingRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if(email.equals(lodgingEntity.getCreatedBy())) {
            LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodgingEntity);

            model.addAttribute("lodgingDto", lodgingDto);
        } else {
            List<RoomDto> roomDtoList = roomService.roomDtoList(id);
            model.addAttribute("lodgingEntity", lodgingEntity);
            model.addAttribute("roomList", roomDtoList);
            model.addAttribute("lodgingErrorMsg", "작성자가 일치하지 않습니다.");
            return "/admin/lodgingContents";
        }


        return "/admin/roomForm";
    }

    @PostMapping(value = "lodging/{lodging_id}/roomRegistration")
    public String newRoom(@PathVariable("lodging_id") Long lodgingId, @Valid LodgingDto lodgingDto, BindingResult bindingResult, Model model, Principal principal, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if(bindingResult.hasErrors()) {
            return "lodging/"+lodgingId;
        }

        if(itemImgFileList.get(0).isEmpty() && lodgingDto.getId() == null){
            model.addAttribute("lodgingErrorMsg", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/lodgingForm";
        }

        String email = principal.getName();

        try {
            roomService.validation(lodgingId, email);
            roomService.saveRoom(lodgingDto, lodgingId, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute(e.getStackTrace());
        }

        return "redirect:/lodging/"+lodgingId;

    }

    @GetMapping(value = "lodging/{lodging_id}/roomUpdate/{room_id}")
    public String fromLodgingDetailToRoomUpdate(@PathVariable("lodging_id") Long lodgingId, @PathVariable("room_id") Long roomId, Model model, Principal principal) {
        String email = principal.getName();

        Lodging lodgingEntity = lodgingRepository.findById(lodgingId).orElseThrow(EntityNotFoundException::new);
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);

        if(email.equals(lodgingEntity.getCreatedBy())) {
            LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodgingEntity);
            RoomDto roomDto = RoomDto.toRoomDto(room);
            lodgingDto.setRoom(room);

            model.addAttribute("lodgingDto", lodgingDto);
            model.addAttribute("roomDto", roomDto);

            return "/admin/roomEdit";
        } else {
            List<RoomDto> roomDtoList = roomService.roomDtoList(lodgingId);
            model.addAttribute("lodgingEntity", lodgingEntity);
            model.addAttribute("roomList", roomDtoList);
            model.addAttribute("lodgingErrorMsg", "작성자가 일치하지 않습니다.");
            return "/admin/lodgingContents";
        }

    }

    @PostMapping(value = "lodging/{lodging_id}/roomUpdate/{room_id}")
    public String updateRoom(@PathVariable("lodging_id") Long lodgingId, @PathVariable("room_id") Long roomId, Model model, @Valid RoomDto roomDto,
                             BindingResult bindingResult, Principal principal) {
        String email = principal.getName();

        if(bindingResult.hasErrors()) {
            return "lodging/"+lodgingId;
        }

        try {
            roomService.validation(lodgingId, email);
            roomService.updateRoom(roomDto, roomId);
        } catch (Exception e) {
            model.addAttribute(e.getStackTrace());
        }

        return "redirect:/lodging/"+lodgingId;

    }

    @GetMapping(value = "lodging/{lodging_id}/roomDelete/{room_id}")
    public String deleteRoom(@PathVariable("lodging_id") Long lodgingId, @PathVariable("room_id") Long roomId, Model model, Principal principal) {
        String email = principal.getName();

        Lodging lodgingEntity = lodgingRepository.findById(lodgingId).orElseThrow(EntityNotFoundException::new);

        if(email.equals(lodgingEntity.getCreatedBy())) {

            try {
                roomService.validation(lodgingId, email);
                roomService.deleteRoom(roomId);
            } catch (IllegalArgumentException e){
                model.addAttribute("error", e.getMessage());
            }

            return "redirect:/lodging/"+lodgingId;

        } else {
            List<RoomDto> roomDtoList = roomService.roomDtoList(lodgingId);
            model.addAttribute("lodgingEntity", lodgingEntity);
            model.addAttribute("roomList", roomDtoList);
            model.addAttribute("lodgingErrorMsg", "작성자가 일치하지 않습니다.");
            return "/admin/lodgingContents";
        }
    }

}
