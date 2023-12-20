package com.example.member.config;


import com.example.member.config.auth.userinfo.OAuth2UserInfo;
import com.example.member.constant.UserRole;
import com.example.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@ToString

public class CustomUserDetails implements UserDetails, OAuth2User {
    private Member member;
    private String email;
    private String password;
    private String nickname;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private OAuth2UserInfo oAuth2UserInfo;

    //----------주문하기 에서 회원 정보 끄내려고 써줌-----------
    private String name;
    private String phoneNumber;
    private boolean authenticated;

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public CustomUserDetails(Member member){
        this.member = member;
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.nickname = member.getNickName();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(member.getUserRole().toString()));
        this.name = member.getName();
        this.phoneNumber = member.getPhoneN1()+member.getPhoneN2()+member.getPhoneN3();
        this.authenticated = true;
    }

    public CustomUserDetails(String email, String password, String nickname, UserRole role , String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(member.getUserRole().toString()));
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authenticated = true;

    }

    public CustomUserDetails(Member member, OAuth2UserInfo oAuth2UserInfo) {
        this.member = member;
        this.oAuth2UserInfo = oAuth2UserInfo;
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.nickname = oAuth2UserInfo.getNickname();  // attributes 대신에 oAuth2UserInfo.getNickname()을 사용
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(member.getUserRole().toString()));
        this.attributes = oAuth2UserInfo.getAttributes();  // attributes 대신에 oAuth2UserInfo.getAttributes()를 사용
        this.authenticated = true;
    }

      //UserDetails 구현 해당 유저의 권한목록 리턴

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getUserRole().toString();
            }
        });
        return collect;
    }
    @Override
//    public String getPassword() {
//        return password;
//    }
    public String getPassword(){
        return member.getPassword();
    }


    @Override
//    public String getUsername() {
//        return email;
//    }
    public String getUsername(){
        return member.getEmail();
    }

//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  true;
    }

    @Override
    public boolean isEnabled() {
        return  true;
    }

    // ... 나머지 UserDetails 인터페이스 메서드 구현

//    public boolean isOAuth2Login() {
//
//        return attributes != null; // attributes가 null인지 여부로 OAuth2 로그인 여부를 판단
//
//    }


//    public String getNickname() {
//        return nickname;
//    }

//    public static String getNickname() {
//        return nickname;
//    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
    //      OAuth2User 구현 @return
    @Override
    public String getName(){
//        String sub = attributes.get("sub").toString();
//        return sub;

        //return oAuth2UserInfo.getProviderId();
        return name;
    }

    //------------주문하기 에서 로그인 한 회원 정보 불러오기 위해 사용(getName 도 수정 함)----------
    public String getPhoneNumber() {
        return phoneNumber;
    }
//
//    @Override
//    public String getProvider() {
//        return provider;
//    }





}
