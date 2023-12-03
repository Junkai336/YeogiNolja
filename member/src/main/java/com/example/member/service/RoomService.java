package com.example.member.service;

import com.example.member.constant.ReservationStatus;
import com.example.member.dto.CommentDto;
import com.example.member.dto.LodgingDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.Comment;
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
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final LodgingRepository lodgingRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    // 매개변수 숙소 id를 제공받고 그 숙소 id의 room을 전부 선택한다.
    // 그 room들을 roomList 받고 roomDtoList로 바꾸기
    public List<RoomDto> roomDtoList(Long lodging_id){
        List<Room> roomList = roomRepository.findAllByLodgingId(lodging_id);
        List<RoomDto> roomDtoList = RoomDto.toRoomDtoList(roomList);
//        for (CommentDto commentDto : commentDtoList){
//            System.out.println(commentDto.toString());
//        }
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
        room.setCheckInTime(roomDto.getCheckInTime());
        room.setCheckOutTime(roomDto.getCheckOutTime());

        // 왠지 모르겠지만 이걸 넣으니까 문제없이 됨 대체 왜?
        roomRepository.save(room);
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);

        List<ItemImg> targetRoomItemImgList = itemImgRepository.findByRoomId(roomId);
        itemImgRepository.deleteAll(targetRoomItemImgList);

        roomRepository.delete(room);
    }
}
