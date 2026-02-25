package scanly.io.scanly_back.notification.domain;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);

    Notification updateStatus(Notification notification);

    List<Notification> findAllByReceiverId(String memberId);

    int countByReceiverIdAndReadFalse(String memberId);

    Optional<Notification> findByIdAndReceiverId(String id, String receiverId);

    void read(Notification notification);

    void deleteAllByReceiverId(String receiverId);
}
