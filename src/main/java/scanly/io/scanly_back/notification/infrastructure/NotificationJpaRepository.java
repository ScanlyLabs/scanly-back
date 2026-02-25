package scanly.io.scanly_back.notification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.notification.infrastructure.entity.NotificationEntity;

import java.util.List;
import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByReceiverId(String memberId);

    int countByReceiverIdAndReadFalse(String receiverId);

    Optional<NotificationEntity> findByIdAndReceiverId(String id, String receiverId);

    void deleteAllByReceiverId(String receiverId);
}
