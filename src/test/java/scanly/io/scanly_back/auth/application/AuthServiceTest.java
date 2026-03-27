package scanly.io.scanly_back.auth.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import scanly.io.scanly_back.IntegrationTestSupport;
import scanly.io.scanly_back.auth.application.dto.command.LoginCommand;
import scanly.io.scanly_back.auth.application.dto.command.ReissueCommand;
import scanly.io.scanly_back.auth.application.dto.info.TokenInfo;
import scanly.io.scanly_back.auth.domain.RefreshToken;
import scanly.io.scanly_back.auth.domain.RefreshTokenRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberRepository;
import scanly.io.scanly_back.member.infrastructure.MemberJpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthService 테스트")
class AuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "RT:";
    private static final String TEST_PASSWORD = "password123";

    @AfterEach
    void after() {
        memberJpaRepository.deleteAllInBatch();
        Set<String> keys = redisTemplate.keys(REFRESH_TOKEN_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Nested
    @DisplayName("로그인 검증")
    class Login {

        @Test
        @DisplayName("[Happy] 올바른 아이디와 비밀번호로 로그인하면 토큰을 발급받는다.")
        void loginSuccess() {
            // given
            Member member = createMember();
            String loginId = member.getLoginId();
            Member savedMember = memberRepository.save(member);

            LoginCommand command = new LoginCommand(loginId, TEST_PASSWORD);

            // when
            TokenInfo info = authService.login(command);

            // then
            assertThat(info.accessToken()).isNotNull();
            assertThat(info.refreshToken()).isNotNull();

            String memberIdFromToken = jwtTokenProvider.getMemberIdFromToken(info.accessToken());
            assertThat(memberIdFromToken).isEqualTo(savedMember.getId());

            Optional<String> token = refreshTokenRepository.findByMemberId(savedMember.getId());
            assertThat(token).isPresent();
        }

        @Test
        @DisplayName("[Bad] 존재하지 않는 아이디로 로그인 시 실패한다.")
        void loginFailMemberNotFound() {
            // given
            LoginCommand command = new LoginCommand("nonexistent", TEST_PASSWORD);

            // when & then
            assertThatThrownBy(() -> authService.login(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("[Bad] 잘못된 비밀번호로 로그인 시 실패한다.")
        void loginFailInvalidPassword() {
            // given
            Member member = createMember();
            String loginId = member.getLoginId();
            memberRepository.save(member);

            LoginCommand command = new LoginCommand(loginId, "wrongPassword");

            // when & then
            assertThatThrownBy(() -> authService.login(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_PASSWORD);
        }
    }

    @Nested
    @DisplayName("로그아웃 검증")
    class Logout {

        @Test
        @DisplayName("[Happy] 로그아웃하면 Refresh Token이 삭제된다.")
        void logoutSuccess() {
            // given
            String memberId = UUID.randomUUID().toString();
            String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
            saveRefreshToken(memberId, refreshToken);

            // when
            authService.logout(memberId);

            // then
            Optional<String> token = refreshTokenRepository.findByMemberId(memberId);
            assertThat(token).isEmpty();
        }
    }

    @Nested
    @DisplayName("토큰 재발급 검증")
    class Reissue {

        @Test
        @DisplayName("[Happy] 유효한 Refresh Token 으로 새 토큰을 발급받는다.")
        void reissueSuccess() {
            // given
            String memberId = UUID.randomUUID().toString();
            String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
            saveRefreshToken(memberId, refreshToken);

            ReissueCommand command = new ReissueCommand(refreshToken);

            // when
            TokenInfo info = authService.reissue(command);

            // then
            assertThat(info.accessToken()).isNotNull();
            assertThat(info.refreshToken()).isNotNull();

            String memberIdFromToken = jwtTokenProvider.getMemberIdFromToken(info.accessToken());
            assertThat(memberIdFromToken).isEqualTo(memberId);

            Optional<String> token = refreshTokenRepository.findByMemberId(memberId);
            assertThat(token).isPresent();
        }

        @Test
        @DisplayName("[Bad] 유효하지 않은 Refresh Token으로 재발급 시 실패한다.")
        void reissueFailInvalidToken() {
            // given
            ReissueCommand command = new ReissueCommand("invalid-refresh-token");

            // when & then
            assertThatThrownBy(() -> authService.reissue(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[Bad] Redis에 저장된 Refresh Token이 없으면 재발급 시 실패한다.")
        void reissueFailTokenNotFound() {
            // given
            String memberId = UUID.randomUUID().toString();
            String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
            // Redis에 저장하지 않음

            ReissueCommand command = new ReissueCommand(refreshToken);

            // when & then
            assertThatThrownBy(() -> authService.reissue(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        @Test
        @DisplayName("[Bad] Redis에 저장된 Refresh Token과 요청 토큰이 다르면 실패한다.")
        void reissueFailTokenMismatch() throws InterruptedException {
            // given
            String memberId = UUID.randomUUID().toString();
            String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
            saveRefreshToken(memberId, refreshToken);

            // 시간차를 두고 다른 토큰 생성
            Thread.sleep(1100);
            String differentToken = jwtTokenProvider.createRefreshToken(memberId);
            ReissueCommand command = new ReissueCommand(differentToken);

            // when & then
            assertThatThrownBy(() -> authService.reissue(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private Member createMember() {
        return Member.signUP(
                "test",
                "테스트",
                passwordEncoder.encode(AuthServiceTest.TEST_PASSWORD),
                "test@test.com"
        );
    }

    private void saveRefreshToken(String memberId, String token) {
        RefreshToken refreshToken = RefreshToken.create(
                memberId,
                token,
                jwtTokenProvider.getRefreshTokenExpiration()
        );
        refreshTokenRepository.save(refreshToken);
    }
}
