package com.example.member.config.auth.userinfo;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;



    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

//    @Override
//    public String getNickname() {
//        if (memberRepository != null) {
//            Member member = memberRepository.findByEmail(getEmail());
//            if (member != null) {
//                return member.getNickname();
//            }
//        }
//        return null;
////        return attributes.get("nickname").toString();
//    }

    @Override
    public String getNickname() {
        String email = getEmail();
        String[] emailParts = email.split("@");
        return emailParts[0];
    }





}
