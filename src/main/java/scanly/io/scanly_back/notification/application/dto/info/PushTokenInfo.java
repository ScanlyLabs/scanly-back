package scanly.io.scanly_back.notification.application.dto.info;

import scanly.io.scanly_back.notification.domain.PushToken;
import scanly.io.scanly_back.notification.domain.model.PushPlatform;

import java.time.LocalDateTime;

public record PushTokenInfo(
        String id,
        String token,
        PushPlatform platform,
        LocalDateTime createdAt
) {
    public static PushTokenInfo from(PushToken savedPushToken) {
        return new PushTokenInfo(
                savedPushToken.getId(),
                savedPushToken.getToken(),
                savedPushToken.getPlatform(),
                savedPushToken.getCreatedAt()
        );
    }
}
