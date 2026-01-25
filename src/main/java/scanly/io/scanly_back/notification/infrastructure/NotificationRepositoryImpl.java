package scanly.io.scanly_back.notification.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.notification.domain.NotificationRepository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;


}
