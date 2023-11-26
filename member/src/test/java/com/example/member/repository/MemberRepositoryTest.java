package com.example.member.repository;

import com.example.member.constant.UserRole;
import com.example.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void createMemberTest() {

        Member member = Member.builder()
                .name("1")
                .email("1")
                .userRole(UserRole.ADMIN)
                .phoneNumber("01012341234")
                .Address("abc")
                        .build();

        memberRepository.save(member);
    }

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
    public void updateTest() {
        Long id = 1L;

        Member member = Member.builder()
                .Id(id)
                .name("hello")
                .build();

        memberRepository.save(member);
    }

    @Test
    public void deleteTest() {
        Long id = 1L;

        memberRepository.deleteById(1L);

    }



}