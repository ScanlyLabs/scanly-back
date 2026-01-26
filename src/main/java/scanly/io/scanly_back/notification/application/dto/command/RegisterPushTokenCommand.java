package scanly.io.scanly_back.notification.application.dto.command;

import scanly.io.scanly_back.notification.domain.model.PushPlatform;

public record RegisterPushTokenCommand(
        String token,
        PushPlatform platform,
        String memberId
) {
}
