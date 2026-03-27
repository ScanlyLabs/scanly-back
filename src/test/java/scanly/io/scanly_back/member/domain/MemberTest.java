package scanly.io.scanly_back.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Member 도메인 테스트")
class MemberTest {

    @Test
    @DisplayName("[Happy] 회원명, 이메일 정보를 수정한다.")
    void updateInfo() {
        // given
        Member member = createMember();
        String name = "새로운 회원명";
        String email = "new@test.com";

        // when
        member.updateInfo(name, email);

        // then
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("[Happy] 비밀번호를 변경한다.")
    void changePassword() {
        // given
        Member member = createMember();
        String newPassword = "new123456";

        // when
        member.changePassword(newPassword);

        // then
        assertThat(member.getPassword()).isEqualTo(newPassword);
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