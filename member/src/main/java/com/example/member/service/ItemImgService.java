package com.example.member.service;

import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.entity.ItemImg;
import com.example.member.entity.Lodging;
import com.example.member.entity.Room;
import com.example.member.repository.ItemImgRepository;
import com.example.member.repository.LodgingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    private final LodgingRepository lodgingRepository;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile)
            throws Exception {

        File uploadDir = new File(itemImgLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

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

    public void saveItemImg(List<MultipartFile> file, Room room) throws Exception {
        for(int i = 0; i < file.size(); i++) {
            ItemImg itemImg = new ItemImg();
            MultipartFile multipartFile = file.get(i);
            saveItemImg(itemImg, multipartFile);
            itemImg.setRoom(room);
            itemImg.setRepimgYn("N");
        }

        List<ItemImg> itemImgList =  itemImgRepository.findByRoomId(room.getId());
        itemImgList.get(0).setRepimgYn("Y");

    }


    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //기존이미지 정보를 가져오기


            //기존 이미지 파일 삭제
            fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
//            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
//                fileService.deleteFile(itemImgLocation + "/" +
//                        savedItemImg.getImgName());
//            }

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



    // List Entity -> List Dto
    public List<ItemImgDto> toItemImgDtos() {
        List<ItemImg> itemImgList = itemImgRepository.findAll();

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.toItemImgDto(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        return itemImgDtoList;
    }

    public void updateItemImg(List<MultipartFile> file, Room room) throws Exception {

        List<ItemImg> itemImgListEdited = new ArrayList<>();

        for(int i = 0; i < file.size(); i++) {
            ItemImg itemImg = new ItemImg();
            MultipartFile multipartFile = file.get(i);
            saveItemImg(itemImg, multipartFile);
            itemImg.setRoom(room);
            itemImgListEdited.add(itemImg);
            itemImg.setRepimgYn("N");
        }
            itemImgListEdited.get(0).setRepimgYn("Y");

    }


    public void deleteFile(ItemImg itemImg) throws Exception {
        fileService.deleteFile(itemImgLocation + "/" + itemImg.getImgName());
    }


    public void deleteImg(Room roomOriginal) throws Exception {
        List<ItemImg> targetRoomItemImgList = itemImgRepository.findByRoomId(roomOriginal.getId());
        for(ItemImg itemImg : targetRoomItemImgList) {
            deleteFile(itemImg);
        itemImgRepository.delete(itemImg);
        }

    }

    public List<ItemImg> findByLodgingId(Long id){

        List<ItemImg> itemImgList = itemImgRepository.findByLodgingId(id);

        return itemImgList;

    }

    public List<ItemImg> findByMainLodgingId(Long id) {
        List<ItemImg> itemImgList = itemImgRepository.findByLodgingIdLimit4(id);
        return itemImgList;
    }
}
