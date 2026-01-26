package scanly.io.scanly_back.notification.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationTemplate {
    CARD_EXCHANGE(
            "명함 교환",
            "%s님이 명함을 보냈습니다."
    );

    private final String title;
    private final String body;

    public String formatBody(String nickname) {
        return String.format(body, nickname);
    }
}
