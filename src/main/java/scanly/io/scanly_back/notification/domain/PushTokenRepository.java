package scanly.io.scanly_back.notification.domain;

import java.util.Optional;

public interface PushTokenRepository {
    PushToken save(PushToken pushToken);
    Optional<PushToken> findByMemberId(String memberId);
    void deleteByMemberId(String memberId);
}
