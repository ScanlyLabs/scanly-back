package scanly.io.scanly_back.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.notification.application.dto.command.RegisterPushTokenCommand;
import scanly.io.scanly_back.notification.application.dto.info.PushTokenInfo;
import scanly.io.scanly_back.notification.domain.PushToken;
import scanly.io.scanly_back.notification.domain.PushTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushTokenService {

    private final PushTokenRepository pushTokenRepository;

    /**
     * 푸시 토큰 등록
     * @param command 토큰 등록 정보
     * @return 등록된 푸시 토큰
     */
    @Transactional
    public PushTokenInfo register(RegisterPushTokenCommand command) {
        PushToken pushToken = PushToken.create(
                command.memberId(),
                command.token(),
                command.platform()
        );
        PushToken savedPushToken = pushTokenRepository.save(pushToken);

        return PushTokenInfo.from(savedPushToken);
    }

    /**
     * 회원 아이디로 푸시 토큰 조회
     * @param memberId 회원 아이디
     * @return 조회된 푸시 토큰
     */
    public PushToken getByMemberId(String memberId) {
        return pushTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PUSH_TOKEN_NOT_FOUND));
    }
}
