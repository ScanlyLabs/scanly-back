package scanly.io.scanly_back.notification.presentation.dto.response;

import scanly.io.scanly_back.notification.application.dto.info.NotificationInfo;
import scanly.io.scanly_back.notification.domain.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        String id,
        NotificationType type,
        String title,
        String body,
        String data,
        boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(NotificationInfo notificationInfo) {
        return new NotificationResponse(
                notificationInfo.id(),
                notificationInfo.type(),
                notificationInfo.title(),
                notificationInfo.body(),
                notificationInfo.data(),
                notificationInfo.isRead(),
                notificationInfo.createdAt()
        );
    }
}
