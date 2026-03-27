package scanly.io.scanly_back.cardbook.domain;

import lombok.Getter;
import scanly.io.scanly_back.cardbook.domain.model.ExchangeStatus;

import java.time.LocalDateTime;

@Getter
public class CardExchange {
    private String id;
    private String senderId;                // 발신자 ID(명함 보내는 사람)
    private String receiverId;              // 수신자 ID(명함 받는 사람)
    private ExchangeStatus status;          // 교환 상태
    private LocalDateTime exchangedAt;      // 교환 요청 일시

    private CardExchange(String id, String senderId, String receiverId, ExchangeStatus status, LocalDateTime exchangedAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.exchangedAt = exchangedAt;
    }

    public static CardExchange of(String id, String senderId, String receiverId, ExchangeStatus status, LocalDateTime exchangedAt) {
        return new CardExchange(id, senderId, receiverId, status, exchangedAt);
    }

    public static CardExchange create(String senderId, String receiverId) {
        return new CardExchange(
                null,
                senderId,
                receiverId,
                ExchangeStatus.PENDING,
                LocalDateTime.now()
        );
    }

    /**
     * 명함 교환 수락 상태로 변경
     */
    public void accept() {
        this.status = ExchangeStatus.ACCEPTED;
    }

    /**
     * 명함 교환 거절 상태로 변경
     */
    public void reject() {
        this.status = ExchangeStatus.REJECTED;
    }

    /**
     * 명함 교환 상태가 대기 상태인지 확인
     */
    public boolean isPending() {
        return this.status == ExchangeStatus.PENDING;
    }
}
