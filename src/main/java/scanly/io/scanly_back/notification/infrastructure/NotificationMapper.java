package scanly.io.scanly_back.notification.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.notification.domain.Notification;
import scanly.io.scanly_back.notification.infrastructure.entity.NotificationEntity;

@Component
public class NotificationMapper {

    /**
     * 알림 도메인 -> 엔티티 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public NotificationEntity toEntity(Notification domain) {
        if (domain == null) {
            return null;
        }

        return NotificationEntity.of(
                domain.getId(),
                domain.getReceiverId(),
                domain.getStatus(),
                domain.getType(),
                domain.getTitle(),
                domain.getBody(),
                domain.getData(),
                domain.isRead()
        );
    }

    /**
     * 알림 엔티티 -> 도메인 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public Notification toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return Notification.of(
                entity.getId(),
                entity.getReceiverId(),
                entity.getStatus(),
                entity.getType(),
                entity.getTitle(),
                entity.getBody(),
                entity.getData(),
                entity.isRead()
        );
    }
}
