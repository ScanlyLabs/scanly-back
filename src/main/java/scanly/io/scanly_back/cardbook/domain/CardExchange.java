package scanly.io.scanly_back.cardbook.domain;

import java.time.LocalDateTime;

public class CardExchange {
    private String id;
    private String senderId;                // 발신자 ID(명함 보내는 사람)
    private String receiverId;              // 수신자 ID(명함 받는 사람)
    private LocalDateTime exchangedAt;         // 교환 일시

    private CardExchange (String id, String senderId, String receiverId, LocalDateTime exchangedAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.exchangedAt = exchangedAt;
    }

    public static CardExchange of(String id, String senderId, String receiverId, LocalDateTime exchangedAt) {
        return new CardExchange(id, senderId, receiverId, exchangedAt);
    }

    public static CardExchange create(String senderId, String receiverId) {
        return new CardExchange(
                null,
                senderId,
                receiverId,
                LocalDateTime.now()
        );
    }


    // getters
    public String getId() {
        return id;
    }
    public String getSenderId() {
        return senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public LocalDateTime getExchangedAt() {
        return exchangedAt;
    }
}
