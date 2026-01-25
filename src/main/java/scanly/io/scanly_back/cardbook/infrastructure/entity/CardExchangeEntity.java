package scanly.io.scanly_back.cardbook.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "card_exchange")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardExchangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sender_id", nullable = false)
    private String senderId;                // 발신자 ID(명함 보내는 사람)

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;              // 수신자 ID(명함 받는 사람)

    @CreatedDate
    @Column(name = "exchanged_at", nullable = false, updatable = false)
    private LocalDateTime exchangedAt;         // 교환 일시

    public static CardExchangeEntity of (String id, String senderId, String receiverId, LocalDateTime exchangedAt) {
        return new CardExchangeEntity(id, senderId, receiverId, exchangedAt);
    }
}
