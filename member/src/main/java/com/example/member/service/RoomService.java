package com.example.member.service;

import com.example.member.constant.ReservationStatus;
import com.example.member.constant.RoomExist;
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
        roomDto.setAdult(lodgingDto.getRoom().getAdult());
        roomDto.setChildren(lodgingDto.getRoom().getChildren());
        roomDto.setCheckInTime(lodgingDto.getRoom().getCheckInTime());
        roomDto.setCheckOutTime(lodgingDto.getRoom().getCheckOutTime());
        roomDto.setReservationStatus(ReservationStatus.AVAILABLE);
//        roomDto.setLodging(Lodging.toLodging(lodgingDto.getMember(),lodgingDto));

        Room room = Room.toRoom(roomDto, lodgingEntity);

        // ***
        lodgingEntity.getRoom().add(room);

        // 이건 되는데 위에꺼가 안됨
        lodgingEntity.setRoomExist(RoomExist.Y);

// =====================================================================================================================
////            System.out.println("savedReserv CheckIn:"+ savedReserv.getRoom().getCheckInTime());
        // checkIn
        String checkIn = room.getCheckInTime();
        String[] checkInSplit =checkIn.split("T");

//            int strsplit11 = Integer.parseInt(strsplit[0]);
//            int strsplit22 = Integer.parseInt(strsplit[1]);
//            int strsplit33 = Integer.parseInt(strsplit[2]);
//            LocalDate localDate = LocalDate.of(strsplit11, strsplit22, strsplit33); // of는 int

        LocalDate localDate1 = LocalDate.parse(checkInSplit[0]); // parse는 string
        DayOfWeek dayOfWeek1 = localDate1.getDayOfWeek();

        int dayOfWeekNumber1 = dayOfWeek1.getValue();
        String day1 ="";
        switch (dayOfWeekNumber1){
            case 1:
                day1= "월";
                break;
            case 2:
                day1= "화";
                break;
            case 3:
                day1= "수";
                break;
            case 4:
                day1= "목";
                break;
            case 5:
                day1= "금";
                break;
            case 6:
                day1= "토";
                break;
            case 7:
                day1= "일";
                break;
        }
        String checkInTime = checkInSplit[0].replaceAll("-","\\.");
        String totalDateIn = checkInTime+"("+day1+")"+" "+checkInSplit[1];

        room.setCheckInTime(totalDateIn);

        // checkOut
        String checkOut = room.getCheckOutTime();
        String[] checkOutSplit =checkOut.split("T");

        LocalDate localDate2 = LocalDate.parse(checkOutSplit[0]); // parse는 string
        DayOfWeek dayOfWeek2 = localDate2.getDayOfWeek();

        int dayOfWeekNumber2 = dayOfWeek2.getValue();
        String day2 ="";
        switch (dayOfWeekNumber2){
            case 1:
                day2= "월";
                break;
            case 2:
                day2= "화";
                break;
            case 3:
                day2= "수";
                break;
            case 4:
                day2= "목";
                break;
            case 5:
                day2= "금";
                break;
            case 6:
                day2= "토";
                break;
            case 7:
                day2= "일";
                break;
        }
        String checkOutTime = checkOutSplit[0].replaceAll("-","\\.");
        String totalDateOut = checkOutTime+"("+day2+")"+" "+checkOutSplit[1];

        room.setCheckOutTime(totalDateOut);
// =====================================================================================================================


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

        // 왠지 모르겠지만 이걸 넣으니까 문제없이 됨 대체 왜?
        roomRepository.save(room);
    }

    public void deleteRoom(Long lodgingId, Long roomId) {


        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);

        Lodging lodgingEntity = lodgingRepository.findById(lodgingId).orElseThrow(EntityNotFoundException::new);

        List<ItemImg> targetRoomItemImgList = itemImgRepository.findByRoomId(roomId);
        itemImgRepository.deleteAll(targetRoomItemImgList);

        roomRepository.delete(room);

        List<Room> roomExistList = roomRepository.findAll();

        for(int i = 0; i < roomExistList.size(); i++) {
            boolean ok = roomExistList.get(i).getLodging().getId().equals(lodgingEntity.getId());
            if(ok == true) {
                return;
            }
        }
        // 목적 : 객실 쪽에서 숙소의 아이디를 가진 객실이 있으면 상태를 변환하지 않는다.
        // 이유 : 숙소가 객실을 가져오지 못하니깐.

    }
}
