package scanly.io.scanly_back.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.member.application.dto.SignUpCommand;
import scanly.io.scanly_back.member.application.dto.SignUpInfo;
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
}
