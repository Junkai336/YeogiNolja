package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemoryMemberRepositoryTest {

    MemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    void save() {
        Member member = new Member();
        member.setName("spring");
        repository.save(member);

//        Member result = repository.findById(member.getId()).get();
        // optional 에서 id를 꺼내려면 get()을 사용한다.
//        Assertions.assertEquals(member, result);
//        System.out.println("Member 정보: " + member);
//        System.out.println("result 정보: " + result);

//        assertThat(member).isEqualTo(result);
    }

    @Test
    void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // 위에서 생성한 객체와 동일한 객체인지 확인

        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1);

        System.out.println("Member 정보 : " + member1);
        System.out.println("result 정보 : " + result);
        assertThat(result).isEqualTo(member1);
    }




    @Test
    void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);
        for (Member member : result) {
            System.out.println(member);
        }
    }
}