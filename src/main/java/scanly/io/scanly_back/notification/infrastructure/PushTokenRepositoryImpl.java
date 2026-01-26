package scanly.io.scanly_back.notification.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.notification.domain.PushToken;
import scanly.io.scanly_back.notification.domain.PushTokenMapper;
import scanly.io.scanly_back.notification.domain.PushTokenRepository;
import scanly.io.scanly_back.notification.infrastructure.entity.PushTokenEntity;

@Repository
@RequiredArgsConstructor
public class PushTokenRepositoryImpl implements PushTokenRepository {

    private final PushTokenJpaRepository pushTokenJpaRepository;
    private final PushTokenMapper pushTokenMapper;

    /**
     * 푸시 토큰 저장
     * @param pushToken 푸시 토큰 정보
     * @return 저장된 푸시 토큰
     */
    @Override
    public PushToken save(PushToken pushToken) {
        PushTokenEntity pushTokenEntity = pushTokenMapper.toEntity(pushToken);
        PushTokenEntity savedPushTokenEntity = pushTokenJpaRepository.save(pushTokenEntity);
        return pushTokenMapper.toDomain(savedPushTokenEntity);
    }
}
