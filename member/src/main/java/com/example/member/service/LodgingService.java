//package com.example.member.service;
//
//import com.example.member.dto.LodgingDto;
//import com.example.member.entity.ItemImg;
//import com.example.member.entity.Lodging;
//import com.example.member.repository.ItemImgRepository;
//import com.example.member.repository.LodgingRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class LodgingService {
//
//    private final LodgingRepository lodgingRepository;
//
//    private final ItemImgService itemImgService;
//
//    private final ItemImgRepository itemImgRepository;
//
//    public Long saveItem(LodgingDto lodgingDto, List<MultipartFile> itemImgFileList) throws Exception{
//
//        //상품 등록
//        Lodging lodging = lodgingDto.createItem();
//        lodgingRepository.save(lodging);
//
//        //이미지등록
//        for(int i=0; i<itemImgFileList.size();i++ ){
//            ItemImg itemImg = new ItemImg();
//            itemImg.setItem(lodging);//해당 이미지 객체에 상품 정보를 연결
//            if(i == 0)
//                itemImg.setRepimgYn("Y"); //이미지넘버가 0 이면 대표이미지
//            else
//                itemImg.setRepimgYn("N");
//            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
//        }
//        return lodging.getId();
//    }
//
//}
