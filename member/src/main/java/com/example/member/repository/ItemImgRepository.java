package com.example.member.repository;

import com.example.member.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
//    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
        List<ItemImg> findByLodgingIdOrderByIdAsc(Long lodgingId);
        List<ItemImg> findByRoomIdOrderByIdAsc(Long roomId);

//    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
        ItemImg findByLodgingIdAndRepimgYn(Long itemId, String repimgYn);
        ItemImg findByRoomIdAndRepimgYn(Long itemId, String repimgYn);

    // findBy 뒤에 조건을 붙이면 , 이를 해석하여 데이터베이스 조회 조건 자동 생성
    // itemId : 조회할 ItemImg 엔티티의 itemId 값입니다.
    // repimgYn : 조회할 ItemImg 엔티티의 repimgYn 값입니다.

        List<ItemImg> findByLodgingId(Long lodgingId);

        List<ItemImg> findByRoomId(Long lodgingId);

}
