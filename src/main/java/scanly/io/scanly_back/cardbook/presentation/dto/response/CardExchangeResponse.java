package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.CardExchangeInfo;

import java.time.LocalDateTime;

public record CardExchangeResponse(
        String id,
        String senderId,
        String receiverId,
        LocalDateTime exchangedAt
) {
    public static CardExchangeResponse from(CardExchangeInfo cardExchangeInfo) {
        return new CardExchangeResponse(
                cardExchangeInfo.id(),
                cardExchangeInfo.senderId(),
                cardExchangeInfo.receiverId(),
                cardExchangeInfo.exchangedAt()
        );
    }
}
