package com.example.member.repository;

import com.example.member.constant.UserRole;
import com.example.member.entity.ItemImg;
import com.example.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;
    @Autowired
    ItemImgRepository itemImgRepository;

    @Test
    public void imgoutPut(){
//     for(int i = 101; i<= 150; i++){
//         System.out.println(
//                 "INSERT INTO cscart_orders (payment_id, order_id) VALUES ("+i+", "+i+"데이터);"
//                 "INSERT INTO `just_board`.`comment` (`id`, `reg_time`, `update_time`, `created_by`, `modified_by`, `comment`, `article_id`, `member_id`)" +
//                         "VALUES ('"+i+"', '2023-12-14 00:32:25.000000', '2023-12-14 00:32:25.681806', '1@1', '1@1', '댓글"+i+"', '1', '1');"
//         );
//     }

    }


//    @Test
//    public void createMemberTest() {
//
//
//        Member member = Member.builder()
//                .name("nameee")
//                .email("a@a")
//                .password("1234")
//                .userRole(UserRole.ADMIN)
////                .phoneNumber("01012341234")
//                .Address("abc")
//                        .build();
//
//        memberRepository.save(member);
//    }

    @Test
    public void findIdTest() {

        Long id = 11L;

        Optional<Member> result = memberRepository.findById(id);

        if(result.isPresent()) {
            Member member = result.get();
            System.out.println(member);
        } else {
            System.out.println("찾을 데이터가 없음");
        }
    }


    @Test
    public void deleteTest() {
        Long id = 1L;

        memberRepository.deleteById(1L);

    }



}