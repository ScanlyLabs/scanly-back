package scanly.io.scanly_back.notification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.notification.infrastructure.entity.NotificationEntity;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, String> {
}
