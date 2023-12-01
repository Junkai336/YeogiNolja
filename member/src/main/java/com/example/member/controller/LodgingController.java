package com.example.member.controller;

import com.example.member.dto.LodgingDto;
import com.example.member.entity.Lodging;
import com.example.member.repository.LodgingRepository;
import com.example.member.service.LodgingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
//@Transactional
@RequestMapping("/lodging")
@RequiredArgsConstructor
public class LodgingController {

    private final LodgingService lodgingService;
    private final LodgingRepository lodgingRepository;


    @GetMapping(value = "/registration")
    public String toRegistration(Model model) {
        LodgingDto lodgingDto = new LodgingDto();
        model.addAttribute("lodgingDto", lodgingDto);

        return "admin/lodgingForm";
    }


    @PostMapping(value = "/registration")
    public String NewLodging(@Valid LodgingDto lodgingDto, BindingResult bindingResult, Model model, Principal principal, RedirectAttributes rttr) {
        if(bindingResult.hasErrors()){
            return "admin/lodgingForm";
        }

        String email = principal.getName();

        try {
            lodgingService.saveItem(lodgingDto, email);
            rttr.addFlashAttribute("lodgingSuccessMsg", "숙소 등록이 완료되었습니다.");
//            lodgingService.saveItem(lodgingDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "admin/lodgingForm";
        }

        return "redirect:/lodging/list";

    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//
//    @PostMapping(value = "/registration")
//    public String NewLodging(@Valid LodgingDto lodgingDto, BindingResult bindingResult,
//                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){
//        if(bindingResult.hasErrors()){
//            return "/admin/lodgingForm";
//        }
//        if(itemImgFileList.get(0).isEmpty() && lodgingDto.getId() == null){
//            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
//            return "/admin/lodgingForm";
//        }
//        try {
//            lodgingService.saveItem(lodgingDto, itemImgFileList);
//        } catch (Exception e){
//            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
//            return "/admin/lodgingForm";
//        }
//
//        return "redirect:/";
//    }
//
    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping(value = {"/list","/list/{page}"})
    public String LodgingManage(Model model) {
        List<LodgingDto> lodgingDtoList = lodgingService.lodgingDtos();
        model.addAttribute("lodgingDtoList", lodgingDtoList);

        return "/admin/lodgingList";
    }

//    public String itemManage(ItemSearchDto itemSearchDto,
//                             @PathVariable("page") Optional<Integer> page, Model model){
//        Pageable pageble  = PageRequest.of(page.isPresent() ? page.get() : 0,3);
//        //  PageRequest - Data jpa 에서 사용하는 페이지 요청객체
//        // of()메서드를 사용하여 페이지 번호와 페이지당 항목수 지정하여 페이지 요청 정보생성
//        //0 는 url 경로에서 받아온 페이지 번호를 확인하고, 값이 없으면 0
//        //3 은 한 페이지당 보여줄 항목수
//
//        Page<Lodging> items = lodgingService.getAdminItemPage(itemSearchDto, pageble);
//        // itemSearchDto를 사용하여 페이지 네이션 된 데이터를 조회
//        model.addAttribute("items",items); //조회도니 페이지 네이션된 데이터 모델 추가
//        model.addAttribute("itemSearchDto", itemSearchDto);// 검색조건 모델에 추가
//        model.addAttribute("maxPage", 5);
//        //한번에 표시할 최대 페이지수를 모델에 추가
//        //이값을 템플릿에서 사용하여 페이지 네이션 UI를 그릴때(렌더링) 활용
//        return "item/itemMng";
//    }

    @GetMapping(value = "/{id}")
    public String show (@PathVariable Long id, Model model) {
//        Lodging lodgingEntity = lodgingRepository.findById(id).orElse(null);
        Lodging lodgingEntity = lodgingRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        model.addAttribute("lodgingEntity", lodgingEntity);

        return "/admin/lodgingContents";
    }

    @GetMapping(value = "/{id}/lodgingForm")
        public String toUpdate(@PathVariable Long id, Model model) {
            LodgingDto lodgingDto = lodgingService.findLodging(id);
            model.addAttribute("lodgingDto", lodgingDto);

            return "/admin/lodgingForm";
        }

        @PostMapping(value = "/{id}/update")
        public String update(@Valid LodgingDto lodgingDto, BindingResult result, Model model, RedirectAttributes rttr) {

        try {
            lodgingService.lodgingUpdate(lodgingDto);
            rttr.addFlashAttribute("lodgingSuccessMsg", "숙소 수정이 완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMsg", result.getFieldError());
        }

            return "redirect:/lodging/list";

        }

    @GetMapping(value = "/{id}/lodgingDelete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Lodging target = lodgingRepository.findById(id).orElse(null);

        if(target != null) {
            lodgingRepository.delete(target);
            rttr.addFlashAttribute("lodgingSuccessMsg", "숙소 삭제가 완료되었습니다.");
        }


        return "redirect:/lodging/list";
    }

}
