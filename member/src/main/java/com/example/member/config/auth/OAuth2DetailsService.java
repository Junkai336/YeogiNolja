package com.example.member.config.auth;


import com.example.member.config.CustomUserDetails;
import com.example.member.config.auth.userinfo.GoogleUserInfo;
import com.example.member.config.auth.userinfo.KakaoUserInfo;
import com.example.member.config.auth.userinfo.NaverUserInfo;
import com.example.member.config.auth.userinfo.OAuth2UserInfo;
import com.example.member.constant.UserRole;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();    // google
        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(provider.equals("kakao")){	//추가
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }
        String providerId = oAuth2UserInfo.getProviderId();	//수정
        String email = oAuth2UserInfo.getEmail();	//수정


        UserRole role = UserRole.USER;

        // 중복 체크
        Member byEmail = memberRepository.findByEmail(email)
                .orElse(null);


        // DB에 이미 등록된 사용자인 경우에는 업데이트 처리
        if (byEmail == null) {
            // 회원 가입 처리
            String username = provider + "_" + providerId;
            String uuid = UUID.randomUUID().toString().substring(0, 6);
            String password = passwordEncoder.encode("패스워드" + uuid);
            String nickname = oAuth2UserInfo.getNickname();

            Member newMember = Member.oauth2Register()
                    .name(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            newMember.setNickName(nickname);

            memberRepository.save(newMember);

            return new CustomUserDetails(newMember, oAuth2UserInfo);
        }

        // DB에 이미 등록된 사용자인 경우에는 업데이트 처리
        byEmail.setProvider(provider);
        byEmail.setProviderId(providerId);
        memberRepository.save(byEmail);

        return new CustomUserDetails(byEmail, oAuth2UserInfo);

    }
}