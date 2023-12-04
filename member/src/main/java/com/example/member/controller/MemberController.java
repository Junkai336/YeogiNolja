package com.example.member.controller;

import com.example.member.dto.ArticleDto;
import com.example.member.dto.MemberFormDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.member.service.ArticleService;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleService articleService;

    @GetMapping(value = "/join")
    public String toJoin(Model model) {
        MemberFormDto memberFormDto = new MemberFormDto();
        model.addAttribute("memberFormDto", memberFormDto);

        return "member/join";
    }

    // @Valid : 들어오는 객체에 대한 검증을 수행한다. (NotNull 등)
    // BindingResult : ModelAttribute를 이용해 매개변수를 Bean에 바인딩할 때 발생한 오류 정보를 받기 위해 선언 / 바인딩이 실패하면 400 에러
    // 검증 오류가 발생할 경우 오류 내용을 보관하는 스프링 프레임워크에서 제공하는 객체
    @PostMapping(value = "/join")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/join";
        }

        try {
            Member member = Member.toMember(memberFormDto, passwordEncoder);
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


    @GetMapping(value = "/login/error")
    public String loginError(Model model){

        model.addAttribute("loginErrorMsg",
                "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }


    @GetMapping(value = "/edit")
    public String myPage(Principal principal, Model model){
        String email = principal.getName();
        Member member = memberService.findByEmail(email);
        MemberFormDto memberFormDto = MemberFormDto.toMemberFormDto(member);
        model.addAttribute("memberFormDto", memberFormDto);

        return "member/edit";
    }


    @PostMapping(value = "/edit")
    public String editMyPage(@Valid MemberFormDto memberFormDto, BindingResult result,
                             Model model){

        if(result.hasErrors()){
            return "member/edit";
        }

        try {
            memberService.edit(memberFormDto);

        } catch (IllegalStateException e){
            model.addAttribute("memberFormDto", memberFormDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "member/edit";
        }

        return "redirect:/";
    }
    @GetMapping(value = "/members")
    public String management(Model model){
        List<MemberFormDto> memberDtoList = memberService.memberList();
        model.addAttribute("memberDtoList" ,memberDtoList);

        return "/admin/members";

    }

    @GetMapping(value = "/detail/{id}")
    public String memberDetailBoard(@PathVariable Long id, Model model) {

        try {
            List<ArticleDto> boardList = articleService.articleDtoList(id);
            model.addAttribute("boardList", boardList);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "/article/articleByUser";
    }

}