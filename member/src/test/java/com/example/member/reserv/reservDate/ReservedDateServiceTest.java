package com.example.member.reserv.reservDate;

import com.example.member.entity.Room;
import com.example.member.reserv.ReservDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ReservedDateServiceTest {


@Autowired
private ReservedDateService reservedDateService;
    @Autowired
    private ReservedDateRepository reservedDateRepository;
    @Test
    void 예약일자_저장() {
        Long room_id = 38L;
        Room room = createRoom(room_id);

        List<LocalDate> useDateList = reservedDateService.toLocalDate("2023-12-30", "2024-01-06");
        System.out.println(useDateList.toString());
        try {
            reservedDateService.saveReservDate(room_id, useDateList);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }



    }

    // String -> LocalDate
    public LocalDate toLocalDate(String dateString){

        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        System.out.println(date);
        System.out.println(date.getClass());
        return date;
    }

    // 두 날짜 차이 구하기
    public int doubleDate(LocalDate checkIn, LocalDate checkOut){
        int dateMinus = Period.between(checkIn, checkOut).getDays();
        System.out.println(dateMinus);
        return dateMinus;
    }

    public List<LocalDate> aroundDate(LocalDate startDate, long countDate){

        List<LocalDate> reservDate = new ArrayList<>();
        reservDate.add(startDate);
        LocalDate sumDate = startDate;
        for(int i  = 0; i< countDate; i++){
            sumDate = sumDate.plusDays(1);
            reservDate.add(sumDate);
        }

        return reservDate;
    }



    public Room createRoom(Long RoomId){
        Room room = new Room();
        room.setId(RoomId);
        room.setName("sweetRoom");
        room.setDetail("스위트룸입니다. 상세정보");
        room.setPrice("315000원");

        return  room;
    }


}