package com.example.member.service;

import com.example.member.constant.ReservationStatus;
import com.example.member.constant.RoomExist;
import com.example.member.dto.ItemImgDto;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.ItemImg;
import com.example.member.entity.Lodging;
import com.example.member.entity.Room;
import com.example.member.repository.ItemImgRepository;
import com.example.member.repository.LodgingRepository;
import com.example.member.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final LodgingRepository lodgingRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final UploadFileService uploadFileService;

    // 매개변수 숙소 id를 제공받고 그 숙소 id의 room을 전부 선택한다.
    // 그 room들을 roomList 받고 roomDtoList로 바꾸기
    public List<RoomDto> roomDtoList(Long lodging_id){
        //        LocalDate defaultNow = LocalDate.now();
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Room> roomList = roomRepository.findAllByLodgingId(lodging_id);
        System.out.println("roomList : "+roomList);
        List<RoomDto> roomDtoList = RoomDto.toRoomDtoList(roomList);

        return roomDtoList;

    }

    // 접근자 유효성 검사
    // 객실을 삭제할 때 객실에는 member가 없으니 숙소의 member를 가지고 온다. (숙소를 올린 사람과 객실을 올린 사람이 같다는 전제 하에.)
    public void validation(Long lodgingId, String email) throws IllegalArgumentException{
        Lodging lodging = lodgingRepository.findById(lodgingId).orElseThrow(EntityNotFoundException::new);

        if (!StringUtils.equals(lodging.getMember().getEmail(), email)){
            throw new IllegalArgumentException("접근 관한이 없습니다.");
        }

    }

    public void saveRoom(LodgingDto lodgingDto, Long lodgingId, List<MultipartFile> itemImgFileList) throws Exception {

        Lodging lodgingEntity = lodgingRepository.findById(lodgingId).orElseThrow(EntityNotFoundException::new);
        System.out.println("hello");
        System.out.println(lodgingEntity);
        // 엔티티에 담기 전 RoomDto를 가져온다.
        RoomDto roomDto = new RoomDto();
        // roomDto에 lodgingDto 정보를 넣는다.
        roomDto.setName(lodgingDto.getRoom().getName());
        roomDto.setPrice(lodgingDto.getRoom().getPrice());
        roomDto.setDetail(lodgingDto.getRoom().getDetail());
        roomDto.setAdult(lodgingDto.getRoom().getAdult());
        roomDto.setChildren(lodgingDto.getRoom().getChildren());
        roomDto.setCheckInTime(lodgingDto.getRoom().getCheckInTime());
        roomDto.setCheckOutTime(lodgingDto.getRoom().getCheckOutTime());
        roomDto.setReservationStatus(ReservationStatus.AVAILABLE);
//        roomDto.setLodging(Lodging.toLodging(lodgingDto.getMember(),lodgingDto));

        Room room = Room.toRoom(roomDto, lodgingEntity);
        System.out.println(roomDto);

        // ***
        lodgingEntity.getRoom().add(room);

        // 이건 되는데 위에꺼가 안됨
        lodgingEntity.setRoomExist(RoomExist.Y);

        roomRepository.save(room);




        //        이미지등록
        for(int i=0; i<itemImgFileList.size();i++ ){
            ItemImg itemImg = new ItemImg();
            itemImg.setRoom(room);//해당 이미지 객체에 상품 정보를 연결
            if(i == 0)
                itemImg.setRepimgYn("Y"); //이미지넘버가 0 이면 대표이미지
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
//        return room.getId();
    }



    public void updateRoom(RoomDto roomDto, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);
//        Room room = roomRepository.findById(roomDto.getId()).orElseThrow(EntityNotFoundException::new);

        room.setName(roomDto.getName());
        room.setPrice(roomDto.getPrice());
        room.setDetail(roomDto.getDetail());
        room.setAdult(roomDto.getAdult());
        room.setChildren(roomDto.getChildren());
        room.setCheckInTime(roomDto.getCheckInTime());
        room.setCheckOutTime(roomDto.getCheckOutTime());
        room.setReservationStatus(roomDto.getReservationStatus());

        // 왠지 모르겠지만 이걸 넣으니까 문제없이 됨 대체 왜?
        roomRepository.save(room);
    }

    public void deleteRoom(Long lodgingId, Long roomId) {


        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);

        Lodging lodgingEntity = lodgingRepository.findById(lodgingId).orElseThrow(EntityNotFoundException::new);

        List<ItemImg> targetRoomItemImgList = itemImgRepository.findByRoomId(room.getId());
        if(!targetRoomItemImgList.isEmpty()){
            itemImgRepository.deleteAll(targetRoomItemImgList);
        }

        roomRepository.delete(room);

        // 객실이 존재하는 숙소 탐색 후 숙소 roomExist 상태 변환
        List<Room> roomList = roomRepository.findAllByLodgingId(lodgingId);

        if(roomList.isEmpty()) {
            System.out.println("리스트가 비어있음");
            lodgingEntity.setRoomExist(RoomExist.N);
        }

    }


    public List<RoomDto> imageLoad(List<RoomDto> roomDtoList) {
        for (int i = 0; i < roomDtoList.size(); i++) {
            // 객실 DTO i번쨰 꺼내오기
            RoomDto roomDto = roomDtoList.get(i);
            // 꺼내온 숙소 DTO의 아이디를 조회하고 아이디에 맞는 이미지들을 리스트로 뽑아오기
            List<ItemImg> itemImgList = itemImgRepository.findByRoomId(roomDto.getId());

            List<ItemImgDto> itemImgDtoList = new ArrayList<>();

            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.toItemImgDto(itemImg);
                itemImgDtoList.add(itemImgDto);
            }

            // 숙소 DTO 대표 imgUrl을 저장하기 위한 과정
            for (int l = 0; l < itemImgDtoList.size(); l++) {
                ItemImgDto itemImgDto = itemImgDtoList.get(l);

                if (itemImgDto.getRepimgYn().equals("Y") && itemImgDto.getRoom().getId().equals(roomDto.getId())) {
                    roomDto.setImgUrl(itemImgDto.getImgUrl());
                }
            }

            // 숙소 DTO에 이미지 DTO 저장
            roomDto.setItemImgDtoList(itemImgDtoList);
            // 다시 숙소 DTO에 저장
            roomDtoList.set(i, roomDto);
        }
        System.out.println("roomDtoListContainImage : "+ roomDtoList);
        return roomDtoList;
    }

    public List<Room> findAllByLodgingId(Long id) {
        List<Room> roomList = roomRepository.findAllByLodgingId(id);
        return roomList;
    }

    public Room findById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);
        return room;
    }

    public void saveRoomJS(Room room) {
        roomRepository.save(room);
    }
}
