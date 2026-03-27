package scanly.io.scanly_back.member.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import scanly.io.scanly_back.IntegrationJpaTestSupport;
import scanly.io.scanly_back.member.infrastructure.MemberJpaRepository;
import scanly.io.scanly_back.member.infrastructure.MemberMapper;
import scanly.io.scanly_back.member.infrastructure.MemberRepositoryImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import({MemberRepositoryImpl.class, MemberMapper.class})
@DisplayName("MemberRepository 테스트")
class MemberRepositoryTest extends IntegrationJpaTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @AfterEach
    void after() {
        memberJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("[Happy] 회원을 저장한다.")
    void save() {
        // given
        Member member = createMember();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember)
                .extracting("loginId", "name", "email")
                .contains(member.getLoginId(), member.getName(), member.getEmail());
    }

    @Test
    @DisplayName("[Happy] 존재하는 로그인 아이디로 회원 존재유무를 확인할 경우 true를 리턴한다.")
    void existsByLoginIdIsTrue() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        // when
        boolean result = memberRepository.existsByLoginId(savedMember.getLoginId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("[Happy] 존재하지 않는 로그인 아이디로 회원 존재유무를 확인할 경우 false를 리턴한다.")
    void existsByLoginIdIsFalse() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        // when
        boolean result = memberRepository.existsByLoginId("testLogin");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Happy] 아이디로 회원을 조회한다.")
    void findById() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get())
                .extracting("id", "loginId", "name", "email")
                .contains(savedMember.getId(), savedMember.getLoginId(), savedMember.getName(), savedMember.getEmail());
    }

    @Test
    @DisplayName("[Happy] 로그인 아이디로 회원을 조회한다.")
    void findByLoginId() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findByLoginId(savedMember.getLoginId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get())
                .extracting("id", "loginId", "name", "email")
                .contains(savedMember.getId(), savedMember.getLoginId(), savedMember.getName(), savedMember.getEmail());
    }

    @Test
    @DisplayName("[Happy] 회원 정보를 수정한다.")
    void update() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);
        savedMember.updateInfo("테스트명", "new@test.com");

        // when
        Member updatedMember = memberRepository.update(savedMember);

        // then
        assertThat(updatedMember)
                .extracting("id", "loginId", "name", "email")
                .contains(savedMember.getId(), savedMember.getLoginId(), savedMember.getName(), savedMember.getEmail());
    }

    @Test
    @DisplayName("[Happy] 아이디로 회원 정보를 삭제한다.")
    void deleteById() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);
        String memberId = savedMember.getId();

        // when
        memberRepository.deleteById(memberId);

        // then
        Optional<Member> foundMember = memberRepository.findById(memberId);
        assertThat(foundMember).isNotPresent();
    }

    @Test
    @DisplayName("[Happy] 회원 탈퇴를 한다.")
    void withdrawal() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);
        savedMember.withdrawal();
        String memberId = savedMember.getId();

        // when
        memberRepository.withdrawal(savedMember);

        // then
        Optional<Member> foundMember = memberRepository.findById(memberId);
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get())
                .extracting("id", "loginId", "name", "email", "status", "withdrawnAt")
                .contains(savedMember.getId(), savedMember.getLoginId(), savedMember.getName(), savedMember.getEmail(), savedMember.getStatus(), savedMember.getWithdrawnAt());
    }

    private Member createMember() {
        return Member.signUP(
                "test",
                "테스트",
                "password123",
                "test@test.com"
        );
    }
}