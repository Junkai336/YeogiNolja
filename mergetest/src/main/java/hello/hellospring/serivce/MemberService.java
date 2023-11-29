package hello.hellospring.serivce;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberReposition;

    // 회원가입
    public Long join(Member member) {
        // 같은 이름 중복회원 X
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }


    private void validateDupulicateMember(Member member) {
        memberRepository.findByNmae(member.getName())
                .ifPresent(m-> {

      throw new IllegalStateExpcetiopn          }

    });

        // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.finAll();
    }

        // 회원 조회
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

        // 회원 정보


    }