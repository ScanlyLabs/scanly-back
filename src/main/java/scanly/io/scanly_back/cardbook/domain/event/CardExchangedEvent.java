package scanly.io.scanly_back.cardbook.domain.event;

import scanly.io.scanly_back.notification.domain.model.NotificationType;

public record CardExchangedEvent(
        String receiverId,
        NotificationType notificationType,
        String title,
        String body,
        String data
) {
    public static CardExchangedEvent of(
            String receiverId, NotificationType type,
            String title, String body, String data
    ) {
        return new CardExchangedEvent(receiverId, type, title, body, data);
    }
}
