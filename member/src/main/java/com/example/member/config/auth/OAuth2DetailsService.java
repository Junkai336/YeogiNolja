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

//    @Autowired
//    AuthMapper authMapper;

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
//
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        String provider = userRequest.getClientRegistration().getRegistrationId();
//        Map<String, Object> userInfo = oAuth2User.getAttributes();
//
//        String username = "";
//        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
//        String email = "";
//
//        switch (provider){
//
//            case "google":
//                username = "google_"+(String) userInfo.get("sub");
//                email = (String) userInfo.get("email");
//                break;
//
//            case "naver":
//                Map<String, Object> response = oAuth2User.getAttributes("response");
//                username = "naver_"+ (String) response.get("id");
//                break;
//
//            case "kakao":
//                Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
//                username = "kakao_" +userInfo.get("id");
//                email = (String) kakaoAccount.get("email");
//                break;
//        }
//
//        if(authMapper.usernameChk(username) == 0) { // 회원이 아닐시
//            SignupDto signupDto = new SignupDto();
//            signupDto.setUsername(username);
//            signupDto.setNickname(username);
//            signupDto.setEmail(email);
//            signupDto.setPassword(password);
//            signupDto.setPhone("");
//            authMapper.signup(signupDto);
//        }
//
//        CustomUserDetails principal  = authMapper.getUser(username);
//
//        return principal;
//
//
//
//    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;


//        String provider = userRequest.getClientRegistration().getRegistrationId();    //google
//        String providerId = oAuth2User.getAttribute("sub");
//        String username = provider+"_"+providerId;           // 사용자가 입력한 적은 없지만 만들어준다
//
//        String uuid = UUID.randomUUID().toString().substring(0, 6);
//        String password = passwordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다
//
//        String email = oAuth2User.getAttribute("email");
//        Role role = Role.USER;
//
//        Member byUsername = memberRepository.findByUsername(username);

        //DB에 없는 사용자라면 회원가입처리
//        if(byUsername == null){
//            // 이메일의 @ 앞부분을 추출하여 nickname으로 저장
//            String[] emailParts = email.split("@");
//            String femail = emailParts[0];
//            byUsername = Member.oauth2Register()
//                    .name(username)
//                    .password(password)
//                    .email(email)
//
//                    .role(role)
//                    .provider(provider)
//                    .providerId(providerId)
//                    .build();
//
//            byUsername.setNickname(femail);
//
//            memberRepository.save(byUsername);
//        }

//        return new CustomUserDetails(byUsername, oAuth2User.getAttributes());
//    }
        String provider = userRequest.getClientRegistration().getRegistrationId();    // google
        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("kakao")){	//추가
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }
//        String providerId = oAuth2User.getAttribute("sub");
        String providerId = oAuth2UserInfo.getProviderId();	//수정

//        String email = oAuth2User.getAttribute("email");
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


            // 이메일의 @ 앞부분을 추출하여 nickname으로 저장
//            String[] emailParts = email.split("@");
//            String nickname = emailParts[0];

            Member newMember = Member.oauth2Register()
                    .name(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
//                    .nickname(nickname)  // Set the nickname here.
//                    .address(new Address())  // Set the address here. You can create a new Address entity.
                    .build();

            newMember.setNickName(nickname);

            memberRepository.save(newMember);

//            return new CustomUserDetails(newMember, oAuth2User.getAttributes());
            return new CustomUserDetails(newMember, oAuth2UserInfo);
        }

        // DB에 이미 등록된 사용자인 경우에는 업데이트 처리
        byEmail.setProvider(provider);
        byEmail.setProviderId(providerId);
//        byEmail.setNickname(nickname);  // Set the nickname here.
//        byEmail.setAddress(new Address());
        memberRepository.save(byEmail);

//        return new CustomUserDetails(byEmail, oAuth2User.getAttributes());
        return new CustomUserDetails(byEmail, oAuth2UserInfo);

    }
}