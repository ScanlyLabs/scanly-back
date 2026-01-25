package scanly.io.scanly_back.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.notification.domain.Notification;
import scanly.io.scanly_back.notification.domain.NotificationRepository;
import scanly.io.scanly_back.notification.domain.model.NotificationType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void send(
            String receiverId, NotificationType type,
            String title, String body, String data
    ) {
        Notification notification = Notification.create(
                receiverId, type, title, body, data
        );
        notificationRepository.save(notification);
    }
}
