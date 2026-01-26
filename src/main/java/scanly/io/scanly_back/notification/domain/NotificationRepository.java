package scanly.io.scanly_back.notification.domain;

import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);

    Notification updateStatus(Notification notification);

    List<Notification> findAllByReceiverId(String memberId);
}
