package scanly.io.scanly_back.notification.application.dto.info;

import scanly.io.scanly_back.notification.domain.Notification;
import scanly.io.scanly_back.notification.domain.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationInfo(
        String id,
        NotificationType type,
        String title,
        String body,
        String data,
        boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationInfo from(Notification notification) {
        return new NotificationInfo(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getBody(),
                notification.getData(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
