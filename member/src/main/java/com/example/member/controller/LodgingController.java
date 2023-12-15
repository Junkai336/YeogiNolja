package com.example.member.controller;

import com.example.member.constant.ReservationStatus;
import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.ItemImg;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import com.example.member.repository.ItemImgRepository;
import com.example.member.repository.LodgingRepository;
import com.example.member.reserv.ReservDto;
import com.example.member.reserv.reservDate.ReservedDateService;
import com.example.member.service.ItemImgService;
import com.example.member.service.LodgingService;
import com.example.member.service.RoomService;
import com.example.member.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
//@Transactional
@RequestMapping("/lodging")
@RequiredArgsConstructor
public class LodgingController {

    private final LodgingService lodgingService;
    private final RoomService roomService;
    private final ItemImgService itemImgService;
    private final UploadFileService uploadFileService;
    private final ReservedDateService reservedDateService;

    private final LodgingRepository lodgingRepository;
    private final ItemImgRepository itemImgRepository;

    @GetMapping(value = "/registration")
    public String toRegistration(Model model) {
        uploadFileService.refreshUploadFileCheck();
        LodgingDto lodgingDto = new LodgingDto();
        model.addAttribute("lodgingDto", lodgingDto);

        return "admin/lodgingForm";
    }


    @PostMapping(value = "/registration")
    public String NewLodging(@Valid LodgingDto lodgingDto, BindingResult bindingResult, Model model, Principal principal, RedirectAttributes rttr, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "admin/lodgingForm";
        }

        if (itemImgFileList.get(0).isEmpty() && lodgingDto.getId() == null) {
            model.addAttribute("lodgingErrorMsg", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "admin/lodgingForm";
        }

        String email = principal.getName();

        try {
            lodgingService.saveItem(lodgingDto, email, itemImgFileList);
            rttr.addFlashAttribute("lodgingSuccessMsg", "숙소 등록이 완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("lodgingErrorMsg", "숙소 등록 중 에러가 발생하였습니다.");
            return "admin/lodgingForm";
        }

        return "redirect:/lodging/list";

    }

    @GetMapping(value = {"/list", "/list/{page}"})
    public String LodgingManage(@PathVariable("page") Optional<Integer> page, Model model) {
        try {
            uploadFileService.emptyUploadFileCheck();
            uploadFileService.backwardUploadFileCheck();

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<LodgingDto> lodgingDtoList = lodgingService.getLodgingList(pageable);

        model.addAttribute("lodgingDtoList", lodgingDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/lodgingList";

    }

    // 일자 등록없이 기본 값(오늘, 내일) 일자로하여 예약이 가능한 방을 보여준다.
//    @GetMapping(value = "/{lodging_id}")
//    public String show(@PathVariable Long lodging_id, Model model) {
//
//        uploadFileService.refreshUploadFileCheck();
//
//        Lodging lodging = lodgingRepository.findById(lodging_id).orElseThrow(EntityNotFoundException::new);
//
//        lodgingService.emptyRoomGrantedLodgingId(lodging_id, lodging);
//
//        LodgingDto lodgingDto = LodgingDto.toLodgingDto(lodging);
//
//        LodgingDto lodgingDtoContainImage = lodgingService.imageLoad(lodgingDto, lodging_id);
//
//        for (ItemImgDto itemImgDto : lodgingDtoContainImage.getItemImgDtoList()) {
//            System.out.println(itemImgDto);
//        }
//
//        Member member = lodging.getMember();
//
//        lodgingDto.setMember(member);
//
//        List<ItemImg> lodgingItemImgList = itemImgService.findByLodgingId(lodging_id);
//
//
//        // ------------------------------------------------------------
////        LocalDate defaultNow = LocalDate.now();
////        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        List<RoomDto> roomDtoList = roomService.roomDtoList(lodging_id);
//
//        for (int i = 0; i < roomDtoList.size(); i++) {
//            // 객실 DTO i번쨰 꺼내오기
//            RoomDto roomDto = roomDtoList.get(i);
//            // 꺼내온 숙소 DTO의 아이디를 조회하고 아이디에 맞는 이미지들을 리스트로 뽑아오기
//            List<ItemImg> itemImgList = itemImgRepository.findByRoomId(roomDto.getId());
//
//            List<ItemImgDto> itemImgDtoList = new ArrayList<>();
//
//            for (ItemImg itemImg : itemImgList) {
//                ItemImgDto itemImgDto = ItemImgDto.toItemImgDto(itemImg);
//                itemImgDtoList.add(itemImgDto);
//            }
//
//            // 숙소 DTO 대표 imgUrl을 저장하기 위한 과정
//            for (int l = 0; l < itemImgDtoList.size(); l++) {
//                ItemImgDto itemImgDto = itemImgDtoList.get(l);
//
//                if (itemImgDto.getRepimgYn().equals("Y") && itemImgDto.getRoom().getId().equals(roomDto.getId())) {
//                    roomDto.setImgUrl(itemImgDto.getImgUrl());
//                }
//            }
//
//            // 숙소 DTO에 이미지 DTO 저장
//            roomDto.setItemImgDtoList(itemImgDtoList);
//            // 다시 숙소 DTO에 저장
//            roomDtoList.set(i, roomDto);
//
//            // -----------------------------------------------------------
//
//            model.addAttribute("lodgingDto", lodgingDtoContainImage);
//            model.addAttribute("checkForm", new ReservDto());
//            model.addAttribute("roomDtoList", roomDtoList);
//            model.addAttribute("lodgingItemImgList", lodgingItemImgList);
//        }
//
//        return "reserv/lodgingReservContennt";
//
//    }

    @GetMapping(value = "/{id}/lodgingForm")
    public String toUpdate(@PathVariable Long id, Model model, Principal principal) {
        String email = principal.getName();

        Lodging lodgingEntity = lodgingService.findById(id);
        LodgingDto lodgingDto = lodgingService.findLodging(id);

        if (email.equals(lodgingEntity.getCreatedBy())) {
            try {
            lodgingDto = lodgingService.getLodgingDtl(id);
            uploadFileService.refreshUploadFileCheck(id);
            model.addAttribute("lodgingDto", lodgingDto);

            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
            }

            return "admin/lodgingForm";

        } else {

            List<LodgingDto> lodgingDtoList = lodgingService.lodgingDtos();

            model.addAttribute("lodgingDtoList", lodgingDtoList);
            model.addAttribute("lodgingErrorMsg", "작성자가 일치하지 않습니다.");

            return "admin/lodgingList";
        }
    }

    @PostMapping(value = "/{id}/update")
    public String update(@Valid LodgingDto lodgingDto, BindingResult result, Model model, RedirectAttributes rttr, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Principal principal) {
        String email = principal.getName();

//        if(result.hasErrors()){
//            return "admin/lodgingForm";
//        }

        if (itemImgFileList.get(0).isEmpty() && lodgingDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "admin/lodgingForm";
        }

        try {
            uploadFileService.havingIdDelete();
            lodgingService.lodgingUpdate(lodgingDto, itemImgFileList);
//            lodgingService.saveItem(lodgingDto, email, itemImgFileList);
            rttr.addFlashAttribute("lodgingSuccessMsg", "숙소 수정이 완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMsg", result.getFieldError());
        }

        return "redirect:/lodging/list";

    }

    @GetMapping(value = "/{id}/lodgingDelete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr, Model model, Principal principal) throws Exception {
        String email = principal.getName();

        Lodging target = lodgingService.findById(id);
        List<Room> targetRoom = roomService.findAllByLodgingId(id);

        if (email.equals(target.getCreatedBy())) {

//            try {
            lodgingService.deleteLodging(id, target, targetRoom);
            rttr.addFlashAttribute("lodgingSuccessMsg", "숙소 삭제가 완료되었습니다.");

//            } catch (Exception e) {
//                model.addAttribute("errorMessage", e.getMessage());
//            }



            return "redirect:/lodging/list";
        } else {
            List<LodgingDto> lodgingDtoList = lodgingService.lodgingDtos();

            model.addAttribute("lodgingDtoList", lodgingDtoList);
            model.addAttribute("lodgingErrorMsg", "작성자가 일치하지 않습니다.");

            return "admin/lodgingList";
        }
    }

    // @RequestPart : multipart/form-data에 특화된 annotation. 여러 복잡한 값을 처리할 때 유용하다 한다.
    @PostMapping(value = "/addRoom")
    @ResponseBody
    public void addRoom(@RequestPart(value = "paramData") Room room,
                        @RequestPart(value = "img", required = false) List<MultipartFile> file,
                        Model model
    ) throws IOException {
        room.setReservationStatus(ReservationStatus.AVAILABLE);
        roomService.saveRoomJS(room);
        uploadFileService.uploadFileGrantedRoomId(room);
        try {
        itemImgService.saveItemImg(file, room);
            } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

    }
}
