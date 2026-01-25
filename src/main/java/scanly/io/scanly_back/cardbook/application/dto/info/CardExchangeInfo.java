package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.cardbook.domain.CardExchange;

import java.time.LocalDateTime;

public record CardExchangeInfo(
        String id,
        String senderId,
        String receiverId,
        LocalDateTime exchangedAt
) {
    public static CardExchangeInfo from(CardExchange savedCardExchange) {
        return new CardExchangeInfo(
                savedCardExchange.getId(),
                savedCardExchange.getSenderId(),
                savedCardExchange.getReceiverId(),
                savedCardExchange.getExchangedAt()
        );
    }
}
