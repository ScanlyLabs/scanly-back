package scanly.io.scanly_back.notification.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import scanly.io.scanly_back.common.entity.BaseEntity;
import scanly.io.scanly_back.notification.domain.model.NotificationStatus;
import scanly.io.scanly_back.notification.domain.model.NotificationType;

@Getter
@Entity
@Table(name = "notification")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "receiver_id", nullable = false, updatable = false)
    private String receiverId;            // 수신자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;                // 알림 전송 상태

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private NotificationType type;                // 알림 타입

    @Column(name = "title", nullable = false, updatable = false)
    private String title;               // 알림 제목

    @Column(name = "body", nullable = false, updatable = false)
    private String body;                // 알림 내용

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", nullable = false, updatable = false)
    private String data;                // 추가 데이터(딥링크용)

    @Column(name = "is_read")
    private boolean read;             // 읽음 여부

    @Column(name = "retry_count", nullable = false)
    private int retryCount;              // 재시도 횟수

    @Column(name = "fail_reason")
    private String failReason;              // 실패 원인

    public static NotificationEntity of(
            String id, String receiverId, NotificationStatus status, NotificationType type,
            String title, String body, String data, boolean read, int retryCount, String failReason
    ) {
        return new NotificationEntity(id, receiverId, status, type, title, body, data, read, retryCount, failReason);
    }
}
