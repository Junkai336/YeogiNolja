package com.example.member.service;


import com.example.member.dto.MemberFormDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
// CheckedException or 예외가 없을 때는 Commit
// UncheckedException이 발생하면 Rollback
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);

        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);



        return   User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getUserRole().toString())
                .build();
    }


    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        return member;
    }

    public void edit(MemberFormDto memberFormDto) {
        Member member = memberRepository.findById(memberFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPostcode(memberFormDto.getPostcode());
        member.setAddress(memberFormDto.getAddress());
        member.setDetailAddress(memberFormDto.getDetailAddress());
        member.setExtraAddress(memberFormDto.getExtraAddress());
        member.setPhoneN1(memberFormDto.getPhoneN1());
        member.setPhoneN2(memberFormDto.getPhoneN2());
        member.setPhoneN3(memberFormDto.getPhoneN3());
//        memberRepository.save(member);
    }

    public List<MemberFormDto> memberList(){
        List<Member> memberList = memberRepository.findAllByUser();
        List<MemberFormDto> memberDtoList = new ArrayList<>();
        for (Member member:memberList){
            MemberFormDto memberFormDto = MemberFormDto.toMemberFormDto(member);
            memberDtoList.add(memberFormDto);
        }
        return memberDtoList;
    }




}





// https://imiyoungman.tistory.com/9