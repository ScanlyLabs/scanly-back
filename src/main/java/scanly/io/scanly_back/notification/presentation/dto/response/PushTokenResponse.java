package scanly.io.scanly_back.notification.presentation.dto.response;

import scanly.io.scanly_back.notification.application.dto.info.PushTokenInfo;
import scanly.io.scanly_back.notification.domain.model.PushPlatform;

import java.time.LocalDateTime;

public record PushTokenResponse(
        String id,
        String token,
        PushPlatform platform,
        LocalDateTime createdAt
) {
    public static PushTokenResponse from(PushTokenInfo pushTokenInfo) {
        return new PushTokenResponse(
                pushTokenInfo.id(),
                pushTokenInfo.token(),
                pushTokenInfo.platform(),
                pushTokenInfo.createdAt()
        );
    }
}
