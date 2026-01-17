package scanly.io.scanly_back.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.auth.application.dto.info.TokenInfo;
import scanly.io.scanly_back.auth.domain.RefreshToken;
import scanly.io.scanly_back.auth.domain.RefreshTokenRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.member.application.MemberService;
import scanly.io.scanly_back.auth.application.dto.command.LoginCommand;
import scanly.io.scanly_back.member.domain.Member;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 및 토큰 발급
     * 1. 회원 조회
     * 2. 비밀번호 검사
     * 3. access token 및 refresh token 발급
     * 4. Redis에 refresh token 저장
     * @param command 로그인 정보
     * @return 로그인 정보
     */
    @Transactional
    public TokenInfo login(LoginCommand command) {
        // 1. 회원 조회
        Member member = memberService.findByLoginId(command.loginId());

        // 2. 비밀번호 검사
        if (!passwordEncoder.matches(command.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. access token 및 refresh token 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        // 4. Redis에 refresh token 저장
        RefreshToken refresh = RefreshToken.create(
                member.getId(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpiration()
        );
        refreshTokenRepository.save(refresh);

        return TokenInfo.from(accessToken, refreshToken);
    }

    /**
     * 로그아웃 (Refresh Token 삭제)
     * @param memberId 회원 ID
     */
    @Transactional
    public void logout(String memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}
