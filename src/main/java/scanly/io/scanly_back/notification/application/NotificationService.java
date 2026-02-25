package scanly.io.scanly_back.notification.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.notification.application.dto.info.NotificationInfo;
import scanly.io.scanly_back.notification.domain.Notification;
import scanly.io.scanly_back.notification.domain.NotificationRepository;
import scanly.io.scanly_back.notification.domain.PushToken;
import scanly.io.scanly_back.notification.domain.model.NotificationStatus;
import scanly.io.scanly_back.notification.domain.model.NotificationType;
import scanly.io.scanly_back.notification.infrastructure.ExpoPushClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PushTokenService pushTokenService;
    private final ExpoPushClient expoPushClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Transactional
    public Notification send(
            String receiverId, NotificationType type,
            String title, String body, String data
    ) {
        Notification notification = Notification.create(
                receiverId, type, title, body, data
        );
        return notificationRepository.save(notification);
    }

    /**
     * 푸시 알림 전송
     * @param receiverId 수신자 아이디
     * @param type 알림 유형
     * @param title 알림 제목
     * @param body 알림 내용
     * @param data 알림 데이터(딥링크용)
     */
    @Transactional
    public void pushExpoNotification(
            String receiverId,  NotificationType type,
            String title, String body, String data,
            Notification notification
    ) {
        // Expo 푸시 알림 전송
        PushToken pushToken = pushTokenService.getByMemberId(receiverId);

        // 푸시 알림 데이터에 알림 유형 추가
        data = addType(data, type);

        boolean success = Boolean.TRUE.equals(expoPushClient.sendPushNotification(pushToken.getToken(), title, body, data).block());

        if (success) {
            notification.changeStatus(NotificationStatus.SENT);
        } else {
            notification.changeStatus(NotificationStatus.FAILED);
        }
        notificationRepository.updateStatus(notification);
    }

    /**
     * 푸시 알림 데이터에 알림 유형 추가
     * @param data 푸시 알림 데이터
     * @param type 알림 유형
     * @return 푸시 알림 데이터
     */
    private String addType(String data, NotificationType type) {
        try {
            // data가 null이거나 빈 문자열일 수도 있으니 방어
            ObjectNode node = (!StringUtils.hasText(data))
                    ? objectMapper.createObjectNode()
                    : (ObjectNode) objectMapper.readTree(data);

            node.put("type", type.name());

            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            log.error("Failed to add notification type to push data. type={}, rawData={}", type, data, e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 알림 목록 조회
     * @param receiverId 수신자 아이디
     * @return 조회된 알림 목록
     */
    public List<NotificationInfo> getAll(String receiverId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId);

        return notifications.stream().map(NotificationInfo::from).toList();
    }

    /**
     * 안 읽은 알림 수 조회
     * @param receiverId 수신자 아이디
     * @return 안 읽은 알림 수
     */
    public int getUnreadCount(String receiverId) {
        return notificationRepository.countByReceiverIdAndReadFalse(receiverId);
    }

    /**
     * 알림 읽음 처리
     * 1. 알림 조회
     * 2. 알림 읽음 처리
     * 3. 안 읽은 알림 수 조회
     * @param receiverId 수신자 아이디
     * @param id 알림 아이디
     * @return 안 읽은 알림 수
     */
    @Transactional
    public int read(String receiverId, String id) {
        // 1. 알림 조회
        Notification notification = getByIdAndReceiverId(id, receiverId);
        // 2. 알림 읽음 처리
        notification.read();
        notificationRepository.read(notification);
        // 3. 안 읽은 알림 수 조회
        return notificationRepository.countByReceiverIdAndReadFalse(receiverId);
    }

    /**
     * 수신자 아이디 및 알림 아이디로 알림 조회
     * @param id 알림 아이디
     * @param receiverId 수신자 아이디
     * @return 조회된 알림
     */
    private Notification getByIdAndReceiverId(String id, String receiverId) {
        return notificationRepository.findByIdAndReceiverId(id, receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    /**
     * 수신자 아이디로 알림 일괄 삭제
     * @param receiverId 수신자 아이디
     */
    @Transactional
    public void deleteAllByReceiverId(String receiverId) {
        notificationRepository.deleteAllByReceiverId(receiverId);
    }
}
