package scanly.io.scanly_back.notification.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.notification.domain.Notification;
import scanly.io.scanly_back.notification.domain.NotificationRepository;
import scanly.io.scanly_back.notification.infrastructure.entity.NotificationEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper notificationMapper;

    /**
     * 알림 저장
     * @param notification 알림 저장
     * @return 저장된 알림
     */
    @Override
    public Notification save(Notification notification) {
        NotificationEntity notificationEntity = notificationMapper.toEntity(notification);
        NotificationEntity savedNotificationEntity = notificationJpaRepository.save(notificationEntity);
        return notificationMapper.toDomain(savedNotificationEntity);
    }

    /**
     * 알림 유형 수정
     * @param notification 알림
     */
    @Override
    public Notification updateStatus(Notification notification) {
        NotificationEntity notificationEntity = notificationMapper.toEntity(notification);
        NotificationEntity savedNotificationEntity = notificationJpaRepository.save(notificationEntity);
        return notificationMapper.toDomain(savedNotificationEntity);
    }

    /**
     * 알림 목록 조회
     * @param memberId 회원 아이디
     * @return 조회된 알림 목록
     */
    @Override
    public List<Notification> findAllByReceiverId(String memberId) {
        List<NotificationEntity> notificationEntities = notificationJpaRepository.findAllByReceiverId(memberId);
        return notificationEntities.stream().map(notificationMapper::toDomain).toList();
    }
}
