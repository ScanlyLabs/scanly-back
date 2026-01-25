package scanly.io.scanly_back.notification.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;
import scanly.io.scanly_back.notification.domain.model.NotificationStatus;
import scanly.io.scanly_back.notification.domain.model.NotificationType;

public class Notification extends BaseDomain {
    private String id;
    private String receiverId;            // 수신자 ID
    private NotificationStatus status;      // 알림 전송 상태
    private NotificationType type;       // 알림 타입
    private String title;               // 알림 제목
    private String body;                // 알림 내용
    private String data;                // 추가 데이터(딥링크용)
    private boolean isRead;             // 읽음 여부

    private Notification(
            String id, String receiverId, NotificationStatus status, NotificationType type,
            String title, String body, String data, boolean isRead
    ) {
        this.id = id;
        this.receiverId = receiverId;
        this.status = status;
        this.type = type;
        this.title = title;
        this.body = body;
        this.data = data;
        this.isRead = isRead;
    }

    public static Notification of(
            String id, String receiverId, NotificationStatus status, NotificationType type,
            String title, String body, String data, boolean isRead
    ) {
        return new Notification(id, receiverId, status, type, title, body, data, isRead);
    }
    public static Notification create(String receiverId, NotificationType type, String title, String body, String data) {
        return new Notification(
                null,
                receiverId,
                NotificationStatus.PENDING,
                type,
                title,
                body,
                data,
                false
        );
    }

    // getters

    public String getId() {
        return id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getData() {
        return data;
    }

    public boolean isRead() {
        return isRead;
    }
}
