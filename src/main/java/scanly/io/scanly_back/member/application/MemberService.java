package scanly.io.scanly_back.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 회원 가입
     * @param command 회원가입 정보
     * @return 신규 회원 정보
     */
    @Transactional
    public SignUpInfo signUp(SignUpCommand command) {
        Member member = Member.signUP(
                command.loginId(),
                command.password(),
                command.password()
        );

        log.info("member: {}", member);
        Member savedMember = memberRepository.save(member);

        return SignUpInfo.from(savedMember);
    }
}
