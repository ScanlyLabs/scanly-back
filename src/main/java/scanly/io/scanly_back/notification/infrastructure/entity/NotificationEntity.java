package scanly.io.scanly_back.notification.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scanly.io.scanly_back.notification.domain.NotificationType;

@Getter
@Entity
@Table(name = "notification")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "member_id", nullable = false, updatable = false)
    private String memberId;            // 수신자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private NotificationType type;                // 알림 타입

    @Column(name = "title", nullable = false, updatable = false)
    private String title;               // 알림 제목

    @Column(name = "body", nullable = false, updatable = false)
    private String body;                // 알림 내용

    @Column(name = "data", nullable = false, updatable = false)
    private String data;                // 추가 데이터(딥링크용)

    @Column(name = "is_read")
    private boolean isRead;             // 읽음 여부

    public static NotificationEntity of(
            String id, String memberId, NotificationType type,
            String title, String body, String data, boolean isRead
    ) {
        return new NotificationEntity(id, memberId, type, title, body, data, isRead);
    }
}
