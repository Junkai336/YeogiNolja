package com.example.member.dto;

import com.example.member.constant.UserRole;
import com.example.member.entity.Member;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long Id;

    private String name;

    // 특정 열에 중복값이 입력되지 않는다?
    private String email;

    private String password;

    private String phoneNumber;

    private String postNm;  // 우편번호

    private String Address; // 주소

    private UserRole userRole;


    public static MemberDto toMemberDto(Member member){
        MemberDto memberDto =new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setEmail(member.getEmail());
        memberDto.setUserRole(member.getUserRole());
        return memberDto;
    }
}