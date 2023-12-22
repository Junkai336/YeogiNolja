package com.example.member.config;

import com.example.member.config.auth.OAuth2DetailsService;
import com.example.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final  MemberService memberService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    OAuth2DetailsService oAuth2DetailsService;
    @Autowired
    public SecurityConfig(MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/")
        ;

        // mvcMatchers에 permitAll로 등록되지 않은 경우 loginPage(member/login) 으로 이동한다.
        http.authorizeRequests()
                .mvcMatchers("/members/**", "/", "/board/**").permitAll()
                .mvcMatchers("/css/**", "js/**", "img/**", "images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.oauth2Login()
                .loginPage("/members/login")
                .userInfoEndpoint() // OAuth2 로그인 성공 후 가져올 설정들
                .userService(oAuth2DetailsService); // 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시




        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        return http.build();
    }



}
