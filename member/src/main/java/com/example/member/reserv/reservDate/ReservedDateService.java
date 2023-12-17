package com.example.member.reserv.reservDate;

import com.example.member.dto.RoomDto;
import com.example.member.entity.Room;
import com.example.member.repository.RoomRepository;
import com.example.member.reserv.Reserv;
import com.example.member.reserv.ReservDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservedDateService {

    private final ReservedDateRepository reservedDateRepository;
    private final RoomRepository roomRepository;


    // String -> LocalDate
    public List<LocalDate> toLocalDate(String startDate, String endDate) {

        LocalDate checkIn = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate checkOut = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        // 두 날짜의 차 // return int
        int countDate = Period.between(checkIn, checkOut).getDays();
        System.out.println(countDate);

        // checkIn ~ checkOut 일자 List로 저장
//     ex_)[2023-12-30, 2023-12-31, 2024-01-01, 2024-01-02, 2024-01-03, 2024-01-04, 2024-01-05, 2024-01-06]
        List<LocalDate> reservDate = new ArrayList<>();
        reservDate.add(checkIn);
        LocalDate sumDate = checkIn;
        for (int i = 0; i < countDate; i++) {
            sumDate = sumDate.plusDays(1);
            reservDate.add(sumDate);
        }
        return reservDate;
    }

    public void saveReservDate(Room room, List<LocalDate> dateList)throws RuntimeException {


        List<LocalDate> savedDateList = savedDateList(room);
        System.out.println("savedDateList : "+savedDateList);
        // 방의 id를 입력받아 신규로 입력받은 예약일자와 겹치는 방은 저장 시 에러코드 표출
        for (LocalDate date : dateList) {
            ReservedDate reservedDate = new ReservedDate();
            reservedDate.setRoom(room);
            reservedDate.setReserved_date(date);
            for(LocalDate savedDate: savedDateList){
                if (date.equals(savedDate)){
                    throw new RuntimeException(date+"일자에 이미 예약이 되어있어 예약이 불가합니다.");
                }
            }
            reservedDateRepository.save(reservedDate);

        }
    }
    // 방 id를 매개변수로 예약되어있는 일자를 List 로 반환받는다.
    public List<LocalDate> savedDateList(Room room){
        List<LocalDate> savedDateList = new ArrayList<>();
        List<ReservedDate> reservedDateList = reservedDateRepository.findByRoomId(room.getId());
        for(ReservedDate reservedDate: reservedDateList){
            savedDateList.add(reservedDate.getReserved_date());
        }
        System.out.println("savedDateList : " + savedDateList.toString());
        return savedDateList;
    }

    // 예약 일자 default
    public List<RoomDto> defaultValidation(List<RoomDto> roomDtoList) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        // 오늘 일자와 내일 일자를 불러온다
        List<LocalDate> checkDateList = new ArrayList<>();
        checkDateList.add(today);
        checkDateList.add(tomorrow);
        List<Long> room_idList = new ArrayList<>();
        for(LocalDate date: checkDateList){
            // 일자를 매개값으로 받아 해당 일자에 예약이 되어있는 방의 id를 모두 받아온다.
            List<Long> findByDateList = reservedDateRepository.findAllByDate(date);
            // 받아온 List를 하나에 List로 합친다.
            room_idList.addAll(findByDateList);
        }
        // 중복제거 stream List
        // 받은 일자 만큼의 반복된 List의 중복 값을 없애준다.
        List<Long> findresult = room_idList.stream()
                .distinct().collect(Collectors.toList());
        // 찾은 id값으로 Room Entity List를 만든다.
        List<Room> reservedRoomList = new ArrayList<>();
        for(Long room_id: findresult){
            Room room = roomRepository.findById(room_id)
                    .orElseThrow(EntityNotFoundException::new);
            reservedRoomList.add(room);
        }
//        전체 조회된 방List에서 인자를 꺼내어
        for (RoomDto roomDto: roomDtoList){
//            어제, 오늘 일자에 예약이 되어있는 방을 기존의 Dto List에서 제거한다.
            for (Room room : reservedRoomList){
                if (roomDto.getId().equals(room.getId())){
                    roomDtoList.remove(roomDto);
                }
            }

        }
        // 전체 조회된 방List중 오늘 내일 예약이 되어있는방 제외
        // 현재 예약 가능한 방들을 보여준다.
        return roomDtoList;
    }

    // 예약일정 설정 시
    public List<RoomDto> defaultValidation(List<RoomDto> roomDtoList, String checkIn, String checkOut) {
        // 오늘 일자와 내일 일자를 불러온다
        List<LocalDate> checkDateList = toLocalDate(checkIn, checkOut);
        int countDays = checkDateList.size()-1;
        List<Long> room_idList = new ArrayList<>();
        for(LocalDate date: checkDateList){
            // 일자를 매개값으로 받아 해당 일자에 예약이 되어있는 방의 id를 모두 받아온다.
            List<Long> findByDateList = reservedDateRepository.findAllByDate(date);
            // 받아온 List를 하나에 List로 합친다.
            room_idList.addAll(findByDateList);
        }
        // 중복제거 stream List
        // 받은 일자 만큼의 반복된 List의 중복 값을 없애준다.
        List<Long> findresult = room_idList.stream()
                .distinct().collect(Collectors.toList());
        // 찾은 id값으로 Room Entity List를 만든다.
        List<Room> reservedRoomList = new ArrayList<>();
        for(Long room_id: findresult){
            Room room = roomRepository.findById(room_id)
                    .orElseThrow(EntityNotFoundException::new);
            reservedRoomList.add(room);
        }
//        전체 조회된 방List에서 인자를 꺼내어
        for (RoomDto roomDto: roomDtoList){
//            어제, 오늘 일자에 예약이 되어있는 방을 기존의 Dto List에서 제거한다.
            for (Room room : reservedRoomList){
                if (roomDto.getId().equals(room.getId())){
                    roomDtoList.remove(roomDto);
                }

            }

        }
        // 전체 조회된 방List중 오늘 내일 예약이 되어있는방 제외
        // 현재 예약 가능한 방들을 보여준다.
        return priceByDate(roomDtoList, countDays);
    }

    public List<RoomDto> priceByDate(List<RoomDto> roomDtoList, int dateCount){
        for(RoomDto roomDto : roomDtoList) {
            // 예약 일자에 따라 표시하는 가격 변경
            int roomPrice = Integer.parseInt(roomDto.getPrice());
            roomDto.setPrice(String.valueOf(roomPrice * dateCount));
        }
        return roomDtoList;
    }
    public void cancleDate(Reserv reserv) {
        String checkInDate = reserv.getCheckIn();
        String checkOutDate = reserv.getCheckOut();
        Room room = reserv.getRoom();
        Long room_id = room.getId();
        List<LocalDate> dateList = toLocalDate(checkInDate, checkOutDate);

        for(LocalDate date: dateList){
            ReservedDate reservedDate = reservedDateRepository.findByDateAndRoom(date, room_id);
            reservedDateRepository.delete(reservedDate);
        }


    }
}



