package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.cardbook.domain.CardExchange;
import scanly.io.scanly_back.cardbook.domain.model.ExchangeStatus;

import java.time.LocalDateTime;

public record CardExchangeInfo(
        String id,
        String senderId,
        String receiverId,
        ExchangeStatus status,
        LocalDateTime exchangedAt
) {
    public static CardExchangeInfo from(CardExchange cardExchange) {
        return new CardExchangeInfo(
                cardExchange.getId(),
                cardExchange.getSenderId(),
                cardExchange.getReceiverId(),
                cardExchange.getStatus(),
                cardExchange.getExchangedAt()
        );
    }
}
