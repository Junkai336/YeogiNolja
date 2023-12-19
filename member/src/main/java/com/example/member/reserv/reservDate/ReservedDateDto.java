package com.example.member.reserv.reservDate;

import com.example.member.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReservedDateDto {

    private Long id;

    private Room room;

    private LocalDate localDate;

    public static ReservedDateDto toReservedDateDto(ReservedDate reservedDate){
        ReservedDateDto reservedDateDto = new ReservedDateDto();
        reservedDateDto.setId(reservedDate.getId());
        reservedDateDto.setRoom(reservedDate.getRoom());
        reservedDateDto.setLocalDate(reservedDate.getReserve_date());
        return reservedDateDto;
    }
}
