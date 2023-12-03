package com.example.member.service;

import com.example.member.entity.ItemImg;
import com.example.member.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile)
            throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            //저장할 경로, 실제 파일명 , 파일의 바이트배열
            imgUrl = "/images/item/" + imgName;
        }
        //저장한 상품 이미지를 불러올경로
        // d:/shop/ 아래 item에저장 하므로
        //상품을 불러오는 경로는 /images/item/
        //앞에 /images붙는 이유는 Webmvcconfig 클래스에 /images/**

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
//saveItemImg(ItemImg itemImg - 업로드된 이미지와 관련된 상품이미지 정보를 가진
    //ItemImg 객체입니다.
// MultipartFile itemImgFile - 업로드할 상품 이미지 파일 나타내는 MultipartFile
    //oriImgName  - 업로드된 이미지 파일의 원래 파일명을 저장


    //   itemImg.updateItemImg(oriImgName, imgName, imgUrl);
    // 업로드된 이미지 파일의 원래파일명, 저장된파일명, 이미지 url을 업데이트
    // 업데이트된 상품 이미지 정보를 데이터베이스에 저장


    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //기존이미지 정보를 가져오기


            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" +
                        savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            //새로운 이미지 파일의 원본 파일 이름을 가져온다.
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;


            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //이미지 정보 엔티티의 필드를 업데이트 합니다.
            //savedItemImg - 영속상태 이므로 데이터를 변경하는것만으로 변경감지 동작
            // 트랜잭션이 끝날때 update 쿼리가 실행
        }
    }
}
