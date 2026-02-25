package scanly.io.scanly_back.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.auth.domain.RefreshTokenRepository;
import scanly.io.scanly_back.card.application.CardService;
import scanly.io.scanly_back.cardbook.application.CardBookService;
import scanly.io.scanly_back.cardbook.application.GroupService;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.notification.application.NotificationService;
import scanly.io.scanly_back.notification.application.PushTokenService;
import scanly.io.scanly_back.member.application.dto.command.ChangePasswordCommand;
import scanly.io.scanly_back.member.application.dto.command.SignUpCommand;
import scanly.io.scanly_back.member.application.dto.command.UpdateMemberCommand;
import scanly.io.scanly_back.member.application.dto.info.ReadMemberInfo;
import scanly.io.scanly_back.member.application.dto.info.SignUpInfo;
import scanly.io.scanly_back.member.application.dto.info.UpdateMemberInfo;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardBookService cardBookService;
    private final CardService cardService;
    private final GroupService groupService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final NotificationService notificationService;
    private final PushTokenService pushTokenService;

    /**
     * 회원 ID로 회원 조회
     * @param id 회원 ID
     * @return 조회된 회원
     */
    public Member findById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 로그인 아이디로 회원 조회
     * @param loginId 로그인 아이디
     * @return 조회된 회원
     */
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 내 정보 조회
     * @param memberId 회원 ID
     * @return 회원 정보
     */
    public ReadMemberInfo readMe(String memberId) {
        Member member = findById(memberId);
        return ReadMemberInfo.from(member);
    }

    /**
     * 내 정보 수정
     * @param memberId 회원 ID
     * @param command 수정 정보
     * @return 수정된 회원 정보
     */
    @Transactional
    public UpdateMemberInfo updateMe(String memberId, UpdateMemberCommand command) {
        Member member = findById(memberId);
        member.updateInfo(command.name(), command.email());
        Member updatedMember = memberRepository.update(member);
        return UpdateMemberInfo.from(updatedMember);
    }

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
                command.name(),
                passwordEncoder.encode(command.password()),
                command.email()
        );

        Member savedMember = memberRepository.save(member);

        return SignUpInfo.from(savedMember);
    }

    /**
     * 비밀번호 변경
     * @param memberId 회원 ID
     * @param command 비밀번호 변경 정보
     */
    @Transactional
    public void changePassword(String memberId, ChangePasswordCommand command) {
        Member member = findById(memberId);

        if (!passwordEncoder.matches(command.currentPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        member.changePassword(passwordEncoder.encode(command.newPassword()));
        memberRepository.update(member);
    }

    /**
     * 로그인 아이디 사용 가능 여부 확인
     * @param loginId 로그인 아이디
     * @return 사용 가능 여부
     */
    public boolean checkLoginIdAvailable(String loginId) {
        return !memberRepository.existsByLoginId(loginId);
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
     * 회원 탈퇴
     * 1. RefreshToken 삭제
     * 2. 알림 삭제
     * 3. 푸시 토큰 삭제
     * 4. 내 명함 삭제 (명함첩 참조 해제, QR 이미지 삭제 포함)
     * 5. 내 명함첩 및 태그 삭제
     * 6. 내 명함첩 그룹 삭제
     * 7. 회원 탈퇴
     * @param memberId 탈퇴할 회원 아이디
     */
    @Transactional
    public void withdrawal(String memberId) {
        // 회원 존재 여부 확인
        Member member = findById(memberId);

        // 1. RefreshToken 삭제
        refreshTokenRepository.deleteByMemberId(memberId);

        // 2. 알림 삭제
        notificationService.deleteAllByReceiverId(memberId);

        // 3. 푸시 토큰 삭제
        pushTokenService.deleteByMemberId(memberId);

        // 4. 내 명함 삭제 (명함첩 참조 해제, QR 이미지 삭제 포함)
        cardService.deleteByMemberIdIfExists(memberId);

        // 5. 내 명함첩 및 태그 삭제
        cardBookService.deleteAllByMemberId(memberId);

        // 6. 내 명함첩 그룹 삭제
        groupService.deleteAllByMemberId(memberId);

        // 7. 회원 탈퇴 -> 기존 URL이 다른 사람의 프로필로 연결되는 문제 방지
        member.withdrawal();
        memberRepository.withdrawal(member);
    }
}
