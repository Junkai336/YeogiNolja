package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지등록
        for(int i=0; i<itemImgFileList.size();i++ ){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);//해당 이미지 객체에 상품 정보를 연결
            if(i == 0)
                itemImg.setRepimgYn("Y"); //이미지넘버가 0 이면 대표이미지
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
    //상품 상세정보를 가져오는 메서드 선언
    List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
    // 해당삼품에 연결된 이미지 정보를 id 순서대로 가져온다.
    List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    //ItemImgDto 객체 리스트를 초기화합니다.
    for(ItemImg itemImg : itemImgList)    {
        ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
        // ItemImgDto 클래스에 정의된 of 메서드를 호출  ItemImg -> ItemImgDto 로 변환하여 반환
        itemImgDtoList.add(itemImgDto);
        //리스트에 추가
    }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        // 해당 id의 상품정보를 데이터베이스에서 가져옵니다. 없으면 예외처리
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        //상품 정보를 ItemFormDto 로 변환합니다.
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        //상품정보 Dto 에 이미지 정보 DTO 리스트를 설정
        return itemFormDto;
    }
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds= itemFormDto.getItemImgIds();
        // 이미지의 id 리스트를  가져와서 itemImgIds -> 이미지 업데이트나 관련작업(조회)

        // 이미지 등록
        for(int i=0 ; i <  itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        //itemImgIds.get(i) -> 상품에 연결된 각이미지 id
        //itemImgFileList.get(i) -> 새로운 이미지 파일
        }
        return item.getId();
    }

@Transactional(readOnly = true)
public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
    return itemRepository.getAdminItemPage(itemSearchDto, pageable);
}
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}














