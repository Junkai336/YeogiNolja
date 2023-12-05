package com.example.member.service;

import com.example.member.dto.CommentDto;
import com.example.member.dto.RoomDto;
import com.example.member.entity.Comment;
import com.example.member.entity.Room;
import com.example.member.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

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

    // RoomService 병합용 주석

}
