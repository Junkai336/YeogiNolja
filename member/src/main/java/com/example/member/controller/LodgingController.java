//package com.example.member.controller;
//
//import com.example.member.dto.LodgingDto;
//import com.example.member.dto.MemberFormDto;
//import com.example.member.service.LodgingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@Controller
//@Transactional
//@RequestMapping("/lodging")
//@RequiredArgsConstructor
//public class LodgingController {
//
//    private final LodgingService lodgingService;
//
//    @GetMapping(value = "/registration")
//    public String toRegistration(Model model) {
//        LodgingDto lodgingDto = new LodgingDto();
//        model.addAttribute("lodgingDto", lodgingDto);
//
//        return "/admin/lodgingForm";
//    }
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
//}
