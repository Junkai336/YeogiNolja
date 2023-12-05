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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final LodgingService lodgingService;
    private final LodgingRepository lodgingRepository;

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    // 숙소 세부정보로 들어간 다음 객실 관리하기 버튼 (리스트)
    @GetMapping(value = "lodging/{id}/roomRegistration")
    public String fromLodgingDetailToRoomCreation(@PathVariable Long id, Model model) {
        Lodging lodgingEntity = lodgingRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodgingEntity);

        model.addAttribute("lodgingDto", lodgingDto);

        // 애초에 숙소 엔티티에 저장된 객실은 숙소 엔티티에 리스트로 저장되어 있기 때문에
        // lodgingDto만 불러와도 무방하다고 본다. (엔티티 -> DTO 변환도 했고.)

        // !! : get, set Room 을 안했는데 나중에 생각
//        List<LodgingDto> lodgingDtoList = lodgingService.lodgingDtos();
//        model.addAttribute("lodgingDtoList", lodgingDtoList);

        return "/admin/roomForm";
    }

    @PostMapping(value = "lodging/{id}/roomRegistration")
    public String newRoom(@PathVariable Long id, @Valid LodgingDto lodgingDto, BindingResult bindingResult) {

        // lodgingDto 엔티티에 있는 room을 쪼개서 room entity에 저장한다.?

        // 일단 엔티티에 저장해야 하니까 가져옴
        Lodging lodgingEntity = lodgingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // 엔티티에 담기 전 RoomDto를 가져온다.
        RoomDto roomDto = new RoomDto();
        // roomDto에 lodgingDto 정보를 넣는다.
        roomDto.setName(lodgingDto.getRoom().getName());
        roomDto.setPrice(lodgingDto.getRoom().getPrice());
        roomDto.setDetail(lodgingDto.getRoom().getDetail());
        roomDto.setCheckInTime(lodgingDto.getRoom().getCheckInTime());
        roomDto.setCheckOutTime(lodgingDto.getRoom().getCheckOutTime());
        roomDto.setReservationStatus(ReservationStatus.AVAILABLE);
//        roomDto.setLodging(Lodging.toLodging(lodgingDto.getMember(),lodgingDto));

        Room room = Room.toRoom(roomDto, lodgingEntity);

        roomRepository.save(room);


//        return "redirect:/lodging/list";
        return "redirect:/lodging/"+id;

    }

}
