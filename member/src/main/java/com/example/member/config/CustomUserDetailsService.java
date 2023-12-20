package com.example.member.config;


import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        CustomUserDetails userDetails = new CustomUserDetails(member);
        userDetails.setAuthenticated(true); // 인증을 받았다고 설정
        userDetails.setNickname(member.getNickName()); // 닉네임 설정

        return userDetails;
        // 사용자 정보에 닉네임 포함
//        return new CustomUserDetails(member.getEmail(), member.getPassword(), member.getNickname(), member.getRole(), member.getName() , member.getPhoneNumber());
//        return new CustomUserDetails(member);

    }
}
