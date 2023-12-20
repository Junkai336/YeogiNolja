package com.example.member.controller;

import com.example.member.article.ArticleDto;
import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.entity.ItemImg;
import com.example.member.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;
    private final LodgingService lodgingService;
    private final UploadFileService uploadFileService;
    private final ItemImgService itemImgService;
    private final RoomService roomService;

    @GetMapping(value = "/")
    public String toHome(Model model) {
        // 메인페이지 추천숙소
        List<LodgingDto> lodgingDtoList = lodgingService.lodgingDtos();
        uploadFileService.emptyUploadFileCheck();
        uploadFileService.backwardUploadFileCheck();

        for (int i = 0; i < lodgingDtoList.size(); i++) {
            // 숙소 DTO i번쨰 꺼내오기
            LodgingDto lodgingDto = lodgingDtoList.get(i);
            // 꺼내온 숙소 DTO의 아이디를 조회하고 아이디에 맞는 이미지들을 리스트로 뽑아오기
            List<ItemImg> itemImgList = itemImgService.findByMainLodgingId(lodgingDto.getId());

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
        System.out.println("lodgingDtoList = "+lodgingDtoList);


        // 메인페이지 공지사항,이벤트
        List<ArticleDto> articleDtoList = mainService.articles();
        model.addAttribute("articleDtoList",articleDtoList);

        return "index";
    }

}


//select title from article where category_status='NOTICE' and category_status='FREE' ORDER BY reg_time desc limit 5;
//select title from article where category_status='NOTICE' ORDER BY reg_time desc limit 5;
//select title from article where category_status='FREE' ORDER BY reg_time desc limit 5;

//select m from Member m where user_role='USER' order by id desc