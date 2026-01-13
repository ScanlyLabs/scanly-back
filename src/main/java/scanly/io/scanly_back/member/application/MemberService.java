package scanly.io.scanly_back.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.member.application.dto.command.LoginCommand;
import scanly.io.scanly_back.member.application.dto.info.LoginInfo;
import scanly.io.scanly_back.member.application.dto.command.SignUpCommand;
import scanly.io.scanly_back.member.application.dto.info.SignUpInfo;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     * @param command 회원가입 정보
     * @return 신규 회원 정보
     */
    @Transactional
    public SignUpInfo signUp(SignUpCommand command) {
        validateDuplicateLoginId(command.loginId());

        Member member = Member.signUP(
                command.loginId(),
                passwordEncoder.encode(command.password()),
                command.email()
        );

        Member savedMember = memberRepository.save(member);

        return SignUpInfo.from(savedMember);
    }

    /**
     * 로그인 아이디 중복 검사
     * @param loginId 로그인 아이디
     */
    private void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
    }

    /**
     * 회원 로그인
     * @param command 로그인 정보
     * @return 로그인 완료한 유저 정보
     */
    public LoginInfo login(LoginCommand command) {
        Member member = memberRepository.findByLoginId(command.loginId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(command.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return LoginInfo.from(member);
    }
}
