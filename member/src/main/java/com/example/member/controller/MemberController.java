package com.example.member.controller;

import com.example.member.dto.MemberDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.member.service.MemberService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/join")
    public String toJoin(Model model) {
        model.addAttribute("memberDto", new MemberDto());

        return "member/join";
    }

    @PostMapping(value = "/join")
    public String newMember(@Valid MemberDto memberDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/join";
        }

        try {
            Member member = Member.toMember(memberDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String toLogin() {
        return "member/login";
    }

    @GetMapping(value = "/member/login/error")
    public String failLogin(){
        return "member/login";
    }



}
