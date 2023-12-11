package com.example.member.reserv;

import com.example.member.constant.LodgingType;
import com.example.member.constant.UserRole;
import com.example.member.entity.Lodging;
import com.example.member.entity.Member;
import com.example.member.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.Period.between;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservControllerTest {
    @Autowired
    private ReservService reservService;



    @Test
    void reserv() {
        Long room_id = 3L;
        Room room = createRoom(room_id);
        Long member_id = 2L;
        // member_id 값을 가지는 예약자 member 생성
        Member member = createMember1(member_id);

        // 예약자 정보 출력
        System.out.println("예약자 이름 :"+member.getName());
        System.out.println("예약자 이메일 :"+member.getEmail());
        String number =member.getPhoneN1()+"-"+member.getPhoneN2()+"-"+member.getPhoneN3();
        System.out.println("예약자 전화번호 " + number);


        // 예약한 방으로 가져올 수 있는 정보
        System.out.println("방의 이름 "+ room.getName());
        System.out.println("방의 가격"+ room.getPrice());
        System.out.println("숙소의 아이디 " +room.getLodging().getId());
//        System.out.println("숙소의 위치" + room.getLodging().getLocation());
        System.out.println("숙소의 판매자 이름" + room.getLodging().getMember().getName());
        System.out.println("판매자 이메일"+ room.getLodging().getMember().getEmail());

        // LocalDate Test
        ReservDto reservDto = new ReservDto();
        reservDto.setCheckIn("2023-12-30");
        reservDto.setCheckOut("2024-01-06");
        // String => LocalDate
        LocalDate checkInDate = toLocalDate(reservDto.getCheckIn());
        LocalDate checkOutDate = toLocalDate(reservDto.getCheckOut());
        // checkOut - checkIn
         int countDate = doubleDate(checkInDate, checkOutDate);  // 4

        List<LocalDate> useDateList = aroundDate(checkInDate, countDate);
        System.out.println(useDateList.toString());
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

        room.setLodging(createLodging());
        return  room;
    }


    public Lodging createLodging(){
        Lodging lodging = new Lodging();
//         판매자 정보
        lodging.setMember(createMember());
        Long lodging_id = 54L;
        lodging.setId(lodging_id);
        lodging.setName("좋은 숙소");
        lodging.setDetail("숙소 상세 정보");
//        lodging.setLocation("가평 어딘가");
        lodging.setLodgingType(LodgingType.POOLVILLA);
        return lodging;
    }

    // 판매자 생성
    public Member createMember(){
        Member member = new Member();
        member.setName("판매자 이름");
        member.setEmail("판매자 이메일");
        member.setPhoneN1("010");
        member.setPhoneN2("1234");
        member.setPhoneN3("4567");
        member.setUserRole(UserRole.USER);
        return member;
    }

    // 구매자 정보   member_id를 통해 사용자를 가져온다. => test에서는 id값을 가진 멤버를 생성
    public Member createMember1(Long member_id){
        Member member = new Member();
        member.setId(member_id);
        member.setName("예약자 이름");
        member.setEmail("예약자 이메일");
        member.setPhoneN1("010");
        member.setPhoneN2("1234");
        member.setPhoneN3("4567");
        member.setUserRole(UserRole.USER);
        return member;
    }
}