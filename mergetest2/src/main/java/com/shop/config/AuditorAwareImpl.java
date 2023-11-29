package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }

}
//현재 인증 정보에서 사용자 이름(일반적으로 이메일 또는 유저네임)을
// 가져와 userId에 할당합니다.